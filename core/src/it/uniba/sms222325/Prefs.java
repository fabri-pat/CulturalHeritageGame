package it.uniba.sms222325;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

import it.uniba.sms222325.entities.BlockEntity;
import it.uniba.sms222325.entities.PlayerEntity;
import it.uniba.sms222325.entities.SoilEntity;

public class Prefs {

    private Preferences daniel;
    private Preferences fabrizio;
    static private Preferences gameSave;
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

    public void saveLastScore(int score) {
        gameSave.putInteger("lastScore", score);
        gameSave.flush();
    }

    static public int getLastScore () {
        return gameSave.getInteger("lastScore", 0);
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

}
