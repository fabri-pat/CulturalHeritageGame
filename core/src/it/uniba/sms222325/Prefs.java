package it.uniba.sms222325;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {

    private Preferences pref;
    private boolean hasSound;
    private int highScore;

    public Prefs() {
        pref = Gdx.app.getPreferences("My Preferences");
        hasSound = pref.getBoolean("hasSound", true);
        highScore = pref.getInteger("highScore", 0);
    }

    public void setSound(boolean hasSound) {
        this.hasSound = hasSound;
        pref.putBoolean("hasSound", hasSound);
        pref.flush();
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
        pref.putInteger("highScore", highScore);
        pref.flush();
    }

    public boolean hasSound() {
        return hasSound;
    }

    public int getHighScore() {
        return highScore;
    }

}
