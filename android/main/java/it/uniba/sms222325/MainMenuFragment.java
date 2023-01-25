package it.uniba.sms222325;

import android.app.usage.NetworkStatsManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button settingsButton = view.findViewById(R.id.settingsButton);
        Button playButton = view.findViewById(R.id.playButton);
        Button leaderBoardButton = view.findViewById(R.id.leaderboardButton);

        FragmentActivity fragmentActivity = getActivity();

        settingsButton.setOnClickListener(lst -> {
            if (fragmentActivity != null) {
                fragmentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new CustomWindowFragment(new SettingsFragment(), "Impostazioni"))
                        .addToBackStack("mainmenu")
                        .commit();
            }
        });

        playButton.setOnClickListener(lst -> {
            if (fragmentActivity != null) {
                fragmentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new GameFragment())
                        .addToBackStack("mainmenu")
                        .commit();
            }
        });

        leaderBoardButton.setOnClickListener(lst -> {
            if (fragmentActivity != null) {

            }
        });

    }
}