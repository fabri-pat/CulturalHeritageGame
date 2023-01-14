package it.uniba.sms222325;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainMenuFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        Button settingsButton = view.findViewById(R.id.settingsButton);
        Button playButton = view.findViewById(R.id.playButton);

        settingsButton.setOnClickListener(lst -> {
            FragmentActivity fragmentActivity = getActivity();
            fragmentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CustomWindowFragment(new SettingsFragment(), "Impostazioni"))
                    .addToBackStack("mainmenu")
                    .commit();
        });

        playButton.setOnClickListener(lst -> {
            FragmentActivity fragmentActivity = getActivity();
            fragmentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new GameFragment())
                    .addToBackStack("mainmenu")
                    .commit();
        });
        return view;
    }
}