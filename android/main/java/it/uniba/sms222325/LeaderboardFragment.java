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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import it.uniba.sms222325.entities.User;
import it.uniba.sms222325.repositories.UserRepository;
import it.uniba.sms222325.repositories.UserSessionSharedManager;

public class LeaderboardFragment extends Fragment {

    private List<User> userList;

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
                Toast.makeText(getContext(), getString(R.string.login_required_text), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, messageToShare);
            startActivity(Intent.createChooser(intent, getString(R.string.share_leaderboard_title_text)));
        });
    }

    private String getMessageShareScore(){
        User user = UserSessionSharedManager.getUserFromSession(getContext());
        if(user.getUsername().isEmpty()){
            return null;
        }
        int position = userList.indexOf(user) + 1;
        return MessageFormat.format(getString(R.string.share_leaderboard_text), user.getBestScore(), position);
    }
}