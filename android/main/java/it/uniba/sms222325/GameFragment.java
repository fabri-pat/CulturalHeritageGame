package it.uniba.sms222325;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import it.uniba.sms222325.entities.User;
import it.uniba.sms222325.repositories.UserRepository;
import it.uniba.sms222325.repositories.UserSessionSharedManager;

public class GameFragment extends AndroidFragmentApplication implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public void onStart() {
        super.onStart();
        UserSessionSharedManager.registerPreference(getContext(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserSessionSharedManager.unregisterPreference(getContext(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initializeForView(new MyGdxGame());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(UserSessionSharedManager.BESTSCORE_KEY)){
            User userToUpdate = UserSessionSharedManager.getUserFromSession(getContext());
            UserRepository.getInstance().updateBestScore(userToUpdate.getUsername(), userToUpdate.getBestScore());
        }
    }
}
