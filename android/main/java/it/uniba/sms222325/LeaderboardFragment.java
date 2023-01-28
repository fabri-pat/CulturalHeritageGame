package it.uniba.sms222325;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.uniba.sms222325.entities.User;
import it.uniba.sms222325.repositories.UserRepository;

public class LeaderboardFragment extends Fragment {

    private List<User> userList;

    public LeaderboardFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.leaderboardRecyclerView);
        Button shareButton = view.findViewById(R.id.shareButton);
        userList = new ArrayList<>();

        UserRepository.getInstance().getLeaderboard().addOnCompleteListener(task -> {
            userList.addAll(task.getResult());
            UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(getContext(), userList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        shareButton.setOnClickListener(l -> {
            String messageToShare = getMessageShareScore();

            if(messageToShare == null){
                Toast.makeText(getContext(), "Accedi per accedere a questa funzionalità.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, messageToShare);
            startActivity(Intent.createChooser(intent, "Condividi la classifica"));
        });
    }

    private String getMessageShareScore(){
        User loggedUser = ((MainActivity)getActivity()).getLoggedUser();

        if (loggedUser != null){
            int position = userList.indexOf(loggedUser) + 1;
            return String.format("Questo è il mio record in Cultural Heritage Game: %s! Mi trovo alla %s° posizione globale!\nRiesci a battermi?", loggedUser.getBestScore(), position);
        } else
            return null;
    }
}