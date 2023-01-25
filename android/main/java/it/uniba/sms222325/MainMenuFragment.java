package it.uniba.sms222325;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
        //bottone "GIOCA"
        Button gameMenuBtn = view.findViewById(R.id.gameMenuBtn);
        //bottone "NUOVA PARTITA"
        Button playButton = view.findViewById(R.id.playButton);
            //rendo il bottone non visibile e non utilizzabile
            playButton.setVisibility(View.INVISIBLE);
            playButton.setEnabled(false);
        //bottone "CONTINUA"
        Button continueBtn = view.findViewById(R.id.continueButton);
            continueBtn.setVisibility(View.INVISIBLE);
            continueBtn.setEnabled(false);
        //bottone "IMPOSTAZIONI"
        Button settingsButton = view.findViewById(R.id.settingsButton);
            settingsButton.setVisibility(View.INVISIBLE);
            //settingsButton.setEnabled(false);
        //bottone "CLASSIFICA"
        Button leaderboardButton = view.findViewById(R.id.leaderboardButton);
            leaderboardButton.setVisibility(View.INVISIBLE);
            leaderboardButton.setEnabled(false);
        //bottone "PROFILO"
        Button profileButton = view.findViewById(R.id.profileButton);
            profileButton.setVisibility(View.INVISIBLE);
            profileButton.setEnabled(false);
        //bottone "MENU"
        Button mainMenuBtn = view.findViewById(R.id.mainMenuBtn);
        FragmentActivity fragmentActivity = getActivity();

        settingsButton.setOnClickListener(lst -> {
            if (fragmentActivity != null){
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

        //listener gameMenuBtn
        gameMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playButton.getVisibility() == View.INVISIBLE && continueBtn.getVisibility() == View.INVISIBLE) {
                    anim((float)1.5, 0, 0, 0, 1000, playButton, true);
                    anim((float)1.5, 0, 0, 0, 1000, continueBtn, true);
                    playButton.setEnabled(true);
                    //TODO: controllare prima se c'è una partita salvata
                    continueBtn.setEnabled(false);
                }
            }
        });

        //listener mainMenuBtn
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float spost = (float)1.6;
                if(settingsButton.getVisibility() == View.INVISIBLE && leaderboardButton.getVisibility() == View.INVISIBLE && profileButton.getVisibility() == View.INVISIBLE){
                    anim(-spost+(float)0.01, spost/32, 0, 0, 1000, settingsButton, true);
                    anim(-spost+(float)0.01, spost/32, spost, (-spost)/32, 1000, leaderboardButton, true);
                    anim(0, 0, spost, (-spost)/32, 1000, profileButton, true);
                }else{
                    anim(spost/32, -spost+(float)0.01, 0, 0, 1000, settingsButton, false);
                    anim(spost/32, -spost+(float)0.01, (-spost)/32, spost, 1000, leaderboardButton, false);
                    anim(0, 0, (-spost)/32, spost, 1000, profileButton, false);
                }
            }
        });

        return view;
    }

    //funzione per animare i bottoni
    public void anim(float fromx, float tox, float fromy, float toy, long duration, View element, boolean cond){
        Animation bottomUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromx, Animation.RELATIVE_TO_SELF,tox, Animation.RELATIVE_TO_SELF,fromy, Animation.RELATIVE_TO_SELF,toy);
        bottomUp.setDuration(duration);
        bottomUp.setFillAfter(true);
        bottomUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(cond)
                    element.setVisibility(View.VISIBLE);
                /*else
                    element.setEnabled(false);*/
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                /*if(cond)
                    element.setEnabled(true);*/
                if(!cond)
                    element.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        element.startAnimation(bottomUp);
    }

}