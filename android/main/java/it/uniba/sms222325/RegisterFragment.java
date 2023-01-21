package it.uniba.sms222325;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import it.uniba.sms222325.entities.User;
import it.uniba.sms222325.repositories.UserRepository;

public class RegisterFragment extends Fragment {
    GoogleSignInClient googleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        Activity myActivity = getActivity();

        if(myActivity != null)
            googleSignInClient = GoogleSignIn.getClient(myActivity, googleSignInOptions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        EditText registerUsername = view.findViewById(R.id.registerUsername);
        EditText registerEmail = view.findViewById(R.id.registerEmail);
        EditText registerPassword = view.findViewById(R.id.registerPassword);
        Button registerButton = view.findViewById(R.id.registerButton);
        SignInButton registerWithGoogleButton = view.findViewById(R.id.sign_in_button);

        registerButton.setOnClickListener(l -> {
            String username = registerUsername.getText().toString().trim();
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            UserRepository userRepository = UserRepository.getInstance();

            userRepository.getUserByUsername(username).addOnCompleteListener(new OnCompleteListener<User>() {
                @Override
                public void onComplete(@NonNull Task<User> task) {
                    if (task.getResult() == null) {
                        userRepository.getUser("email", email)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.getResult() == null) {
                                        userRepository.addUser(new User(username, password));
                                        firebaseAuth.createUserWithEmailAndPassword(email, password);
                                    } else {
                                        registerEmail.setError("Email already in use.");
                                    }
                                });
                    } else {
                        Log.d(this.getClass().toString(), "User already exists");
                        registerUsername.setError("Username already exists.");
                    }
                }
            });
        });
        return view;
    }



}