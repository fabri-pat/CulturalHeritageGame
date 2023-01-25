package it.uniba.sms222325;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import it.uniba.sms222325.entities.User;
import it.uniba.sms222325.repositories.UserRepository;

public class RegisterFragment extends Fragment {
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Activity myActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        myActivity = getActivity();

        if(myActivity != null)
            googleSignInClient = GoogleSignIn.getClient(myActivity, googleSignInOptions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText registerUsername = view.findViewById(R.id.registerUsername);
        EditText registerEmail = view.findViewById(R.id.registerEmail);
        EditText registerPassword = view.findViewById(R.id.registerPassword);
        Button registerButton = view.findViewById(R.id.registerButton);
        SignInButton registerWithGoogleButton = view.findViewById(R.id.sign_in_button);
        TextView loginTextView = view.findViewById(R.id.loginTextView);
        UserRepository userRepository = UserRepository.getInstance();

        loginTextView.setOnClickListener(l -> {
            View v = (View)l.getParent().getParent().getParent().getParent();

            RotateAnimation rotateAnimation = new RotateAnimation(180, 360, v.getPivotX(), v.getPivotY());
            rotateAnimation.setFillAfter(false);
            rotateAnimation.setDuration(400);
            v.startAnimation(rotateAnimation);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerSettings, new LoginFragment())
                    .commit();
        });

        registerButton.setOnClickListener(l -> {
            String username = registerUsername.getText().toString().trim();
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();

            if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
                if (username.isEmpty())
                    registerUsername.setError("Questo campo è obbligatorio.");
                if (email.isEmpty())
                    registerEmail.setError("Questo campo è obbligatorio");
                if (password.isEmpty())
                    registerPassword.setError("questo campo è obbligatorio");
                return;
            }

            try {
                userRepository.getUserByUsername(username).addOnCompleteListener(new OnCompleteListener<User>() {
                    @Override
                    public void onComplete(@NonNull Task<User> task) {

                        if (task.getResult() == null) {
                            userRepository.getUser("email", email)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.getResult() == null) {
                                            userRepository.addUser(new User(username, email));
                                            firebaseAuth.createUserWithEmailAndPassword(email, password);
                                            Toast.makeText(getContext(), "Benvenuto " + username, Toast.LENGTH_SHORT).show();
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
            } catch (IllegalStateException | IllegalArgumentException | RuntimeExecutionException e) {
                Toast.makeText(getContext(), "Qualcosa è andato storto. Verificare la connesione internet e riprovare", Toast.LENGTH_SHORT).show();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                        String email = googleSignInAccountTask.getResult().getEmail();
                        String username = registerUsername.getText().toString();

                        UserRepository.getInstance().getUser("email", email).addOnCompleteListener(task -> {
                            if (task.getResult() == null) {
                                String idToken = googleSignInAccountTask.getResult().getIdToken();

                                if (idToken != null) {
                                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                                    firebaseAuth.signInWithCredential(firebaseCredential)
                                            .addOnCompleteListener(myActivity, task1 -> {
                                                if (task1.isSuccessful()) {
                                                    if (email != null){
                                                        UserRepository.getInstance()
                                                                .addUser(new User(username, email))
                                                                .addOnCompleteListener(
                                                                        task2 -> Toast.makeText(getContext(), "Benvenuto " + username, Toast.LENGTH_SHORT).show()
                                                                );
                                                    } else {
                                                        Toast.makeText(getContext(), "Qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getContext(), "Benvenuto " + username, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }else {
                                Toast.makeText(getContext(), "Account google già collegato ad un altro utente.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        registerWithGoogleButton.setOnClickListener(s -> {
            String username = registerUsername.getText().toString().trim();

            if(username.isEmpty()){
                registerUsername.setError("Insert username");
                return;
            }

            userRepository.getUserByUsername(username).addOnCompleteListener(task -> {
                if (task.getResult() == null) {
                    Intent signupWithGoogleIntent = googleSignInClient.getSignInIntent();
                    activityResultLauncher.launch(signupWithGoogleIntent);
                }else {
                    registerUsername.setError("Username already in use.");
                }
            });
        });
    }
}