package it.uniba.sms222325.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import it.uniba.sms222325.entities.User;

public class UserSessionSharedManager {

    public static final String USER_SESSION_SHARED_PREFERENCES_NAME = "UserSession";
    public static final String USERNAME_KEY = "username";
    public static final String EMAIL_KEY = "email";
    public static final String BESTSCORE_KEY = "bestScore";
    private static SharedPreferences userSessionSharedPreferences;

    public static void saveUserSession(Context context, User user){
        userSessionSharedPreferences = context.getSharedPreferences(USER_SESSION_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSessionSharedPreferences.edit();
        editor.putString(USERNAME_KEY, user.getUsername());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putInt(BESTSCORE_KEY, user.getBestScore());
        editor.apply();
    }

    public static User getUserFromSession(Context context){
        userSessionSharedPreferences = context.getSharedPreferences(USER_SESSION_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String username = userSessionSharedPreferences.getString(USERNAME_KEY, "");
        String email = userSessionSharedPreferences.getString(EMAIL_KEY, "");
        int bestScore = userSessionSharedPreferences.getInt(BESTSCORE_KEY, 0);
        return new User(username, email, bestScore);
    }

    public static void deleteUserSession(Context context) {
        userSessionSharedPreferences = context.getSharedPreferences(USER_SESSION_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSessionSharedPreferences.edit();
        editor.remove(USERNAME_KEY);
        editor.remove(EMAIL_KEY);
        editor.remove(BESTSCORE_KEY);
        editor.apply();
    }

    public static void registerPreference(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SESSION_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterPreference(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SESSION_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
