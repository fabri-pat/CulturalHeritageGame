package it.uniba.sms222325;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.uniba.sms222325.repositories.UserSessionSharedManager;

public class SettingsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserSessionSharedManager.registerPreference(getContext(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        UserSessionSharedManager.unregisterPreference(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button accountButton = view.findViewById(R.id.account_button);

        showAccountFragment();

        accountButton.setOnClickListener(l -> showAccountFragment());
    }

    public void showAccountFragment(){
        if(UserSessionSharedManager.getUserFromSession(getContext()).getUsername().isEmpty()){
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerSettings, new LoginFragment())
                    .commit();
        } else{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerSettings, new LogoutFragment())
                    .commit();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(UserSessionSharedManager.USERNAME_KEY)){
            showAccountFragment();
        }
    }
}