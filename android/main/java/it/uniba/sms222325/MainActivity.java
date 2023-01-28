package it.uniba.sms222325;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import it.uniba.sms222325.entities.User;


public class MainActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks{

    final int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loggedUser = setLoggedUserIfExists();

        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new MainMenuFragment())
                .commit();
    }

    /**
     * When the main activity starts check if user is already logged-in.
     *
     * TODO: Get from remote repository user profile
     * TODO: In account settings hide sign-in button and show logout button, if user isn't logged-in on the contrary
     * TODO: In main menu set clickable leaderboard button, if user ins't logged-in on the contrary
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void exit() {
    }

    private User setLoggedUserIfExists(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_session_shared_preferences), Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username.isEmpty())
            return null;

        String email = sharedPreferences.getString("email", "");
        int bestScore = sharedPreferences.getInt("bestScore", 0);

        return new User(username, email, bestScore);
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}