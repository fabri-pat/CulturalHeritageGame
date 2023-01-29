package it.uniba.sms222325;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {

    private Preferences daniel;
    private Preferences fabrizio;
    private boolean hasSound;
    private int bestScore;
    private String username;

    public Prefs() {
        daniel = Gdx.app.getPreferences("My Preferences");
        fabrizio = Gdx.app.getPreferences("UserSession");
        hasSound = daniel.getBoolean("hasSound", true);
        bestScore = fabrizio.getInteger("bestScore", 0);
        username = fabrizio.getString("username", null);
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
        fabrizio.putInteger("bestScore", bestScore);
        fabrizio.flush();
    }

    public boolean hasSound() {
        return hasSound;
    }

    public int getBestScore() {
        return bestScore;
    }

    public String getUsername() {
        return username;
    }

}
