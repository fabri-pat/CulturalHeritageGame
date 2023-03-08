package it.uniba.sms222325;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import it.uniba.sms222325.entities.BlockEntity;
import it.uniba.sms222325.entities.PlayerEntity;
import it.uniba.sms222325.entities.SoilEntity;

public class Prefs {

    private Preferences daniel;
    private Preferences fabrizio;
    private Preferences gameSave;
    private boolean hasSound;
    private int bestScore;
    private String username;

    public Prefs() {
        daniel = Gdx.app.getPreferences("My Preferences");
        fabrizio = Gdx.app.getPreferences("UserSession");
        hasSound = daniel.getBoolean("hasSound", true);
        bestScore = fabrizio.getInteger("bestScore", 0);
        username = fabrizio.getString("username", null);

        gameSave = Gdx.app.getPreferences("save");
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
        fabrizio.putInteger("bestScore", bestScore);
        fabrizio.flush();
    }

    public boolean hasSound() {
        return hasSound;
    }

    public void setHasSound(boolean newHasSound) {
        this.hasSound=newHasSound;
        daniel.putBoolean("hasSound",hasSound);
        daniel.flush();
    }

    public int getBestScore() {
        return bestScore;
    }

    public String getUsername() {
        return username;
    }


    public void saveCurrentGame(PlayerEntity player, List<BlockEntity> blocks, int score, int city) {
        gameSave.putInteger("score", score);
        gameSave.putInteger("city", city);
        System.out.println("Saved Score: " + score + "; city: " + city);

        gameSave.putFloat("playerX", player.getPosition().x);
        gameSave.putFloat("playerY", player.getPosition().y);
        gameSave.putFloat("playerXSpeed", player.getActualSpeed());
        gameSave.putFloat("playerYSpeed", player.getVerticalSpeed());
        System.out.println("Saved All player data\n" + player.getActualSpeed() + "; "
                + player.getPosition().x + "; " + player.getPosition().y);


        int i = 0;
        for (BlockEntity block: blocks) {
            gameSave.putFloat("block" + i, block.getPosition().x);
            i++;
        }
        System.out.println("Saved All " + i + " Blocks");

        gameSave.putBoolean("saved", true);

        gameSave.flush();
    }

    public void deleteCurrentGame() {
        gameSave.putBoolean("saved", false);
        gameSave.flush();
    }

    public boolean isGameSaved()
    {
        return gameSave.getBoolean("saved", false);
    }

    public List<Float> getSavedBlocks() {
        List<Float> list = new ArrayList<Float>();
        int i = 0;
        float value;
        while(true) {
            value = gameSave.getFloat("block" + i, -99f);
            if(value != -99f) {
                list.add(value);
                i++;
            } else break;
        }
        System.out.println("Blocchi trovati: " + i);
        return list;
    }

    public List<Float> getSavedPlayer() {
        List<Float> list = new ArrayList<Float>();
        float value;

        list.add(gameSave.getFloat("playerX"));
        list.add(gameSave.getFloat("playerY"));
        list.add(gameSave.getFloat("playerXSpeed"));
        list.add(gameSave.getFloat("playerYSpeed"));

        return list;
    }

    public int getSavedScore() {
        return gameSave.getInteger("score");
    }

    public int getSavedCity() {
        return gameSave.getInteger("city");
    }










    public void saveCurrentGameJson(PlayerEntity player, List<SoilEntity> soils, List<BlockEntity> blocks, int score, int city) {
        Hashtable<String, String> hashTable = new Hashtable<String, String>();
        Json json = new Json();
        System.out.println("Creati Oggetti");
        hashTable.put("soils", json.toJson(soils.toArray()) ); //here you are serializing the array
        System.out.println("hash soils");
        hashTable.put("blocks", json.toJson(blocks.toArray()));
        System.out.println("hash blocks");
        hashTable.put("player", json.toJson(player));
        System.out.println("Hash player");

        gameSave.putString("soils", hashTable.get("soils"));
        System.out.println("put soils");
        gameSave.putString("blocks", hashTable.get("blocks"));
        System.out.println("put blocks");
        gameSave.putString("player", hashTable.get("player"));
        System.out.println("put player");
        //putting the map into preferences
        //String serializedInts = Gdx.app.getPreferences("preferences").getString("test");
        //int[] deserializedInts = json.fromJson(int[].class, serializedInts); //you need to pass the class type - be aware of it!

        gameSave.putInteger("score", score);
        gameSave.putInteger("city", city);
        gameSave.putBoolean("saved", true);
    }

    public PlayerEntity getDeserializedSavedPlayer() {
        String serializedPlayer = gameSave.getString("player");
        PlayerEntity deserializedPlayer = json.fromJson(PlayerEntity.class, serializedPlayer);
        return deserializedPlayer;
    }

    public List<BlockEntity> getDeserializedSavedBlocks() {
        String serializedBlocks = gameSave.getString("blocks");
        BlockEntity[] deserializedBlocks = json.fromJson(BlockEntity[].class, serializedBlocks);
        List<BlockEntity> list = Arrays.asList(deserializedBlocks);
        return list;
    }

}
