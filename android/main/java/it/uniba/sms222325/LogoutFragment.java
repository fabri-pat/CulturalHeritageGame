package it.uniba.sms222325;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import it.uniba.sms222325.repositories.UserSessionSharedManager;


public class LogoutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button logoutButton = view.findViewById(R.id.logoutButton);
        TextView helloTextView = view.findViewById(R.id.logoutTextView);

        String helloMessage = "Ciao, " + UserSessionSharedManager.getUserFromSession(getContext()).getUsername();
        helloTextView.setText(helloMessage);

        logoutButton.setOnClickListener(l -> logoutUser());
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        UserSessionSharedManager.deleteUserSession(getContext());
    }
}