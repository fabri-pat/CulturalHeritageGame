package it.uniba.sms222325;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import it.uniba.sms222325.scene2d.GameOverScreen;
import it.uniba.sms222325.scene2d.LoadingScreen;
import it.uniba.sms222325.scene2d.MenuScreen;

public class MyGdxGame extends Game {

    private AssetManager manager;       // con questo gestisco tutti i media in maniera centralizzata

    public LoadingScreen loadingScreen;
    public MenuScreen menuScreen;
    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;

    public AssetManager getManager() {
        return manager;
    }

    @Override
    public void create() {
        manager = new AssetManager();
        manager.load("player.png", Texture.class);
        manager.load("block.png", Texture.class);
        manager.load("soil.png", Texture.class);
        manager.load("audio/jump.ogg", Sound.class);
        manager.load("audio/die.ogg", Sound.class);
        manager.load("audio/song.ogg", Music.class);
        manager.load("gameover.png", Texture.class);
        manager.load("cities/Firenze1.png", Texture.class);
        manager.load("cities/Pompei1.png", Texture.class);
        manager.load("cities/Segesta1.png", Texture.class);
        manager.load("cities/Roma1.png", Texture.class);
        //manager.load("logo.png", Texture.class);
        manager.finishLoading();

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public void finishLoading() {
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setScreen(gameScreen);
    }
}
