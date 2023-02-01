package it.uniba.sms222325;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Pattern;

import it.uniba.sms222325.entities.User;
import it.uniba.sms222325.repositories.UserRepository;
import it.uniba.sms222325.repositories.UserSessionSharedManager;

public class LoginFragment extends Fragment {
    private GoogleSignInClient googleSignInClient;
    private FragmentActivity myActivity;
    private UserRepository userRepository;
    private SharedPreferences userSessionSharedPreferences;

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

        userRepository = UserRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        EditText loginEmail = view.findViewById(R.id.loginEmail);
        EditText loginPassword = view.findViewById(R.id.loginPassword);
        Button loginButton = view.findViewById(R.id.loginButton);
        SignInButton loginWithGoogleButton = view.findViewById(R.id.sign_in_button);
        View registerPageText = view.findViewById(R.id.registerTextView);

        registerPageText.setOnClickListener(x -> {

            //TODO:Improve animation
            View v = (View)x.getParent().getParent().getParent().getParent();

            RotateAnimation rotateAnimation = new RotateAnimation(180, 360, v.getPivotX(), v.getPivotY());
            rotateAnimation.setFillAfter(false);
            rotateAnimation.setDuration(400);
            v.startAnimation(rotateAnimation);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerSettings, new RegisterFragment())
                    .commit();
        });

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty())
                    loginEmail.setError(getString(R.string.label_field_required_error));
                if (password.isEmpty())
                    loginPassword.setError(getString(R.string.label_field_required_error));
                return;
            }

            if (Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), email))
                firebaseAuth
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(s -> {
                            Toast.makeText(this.getContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                            userRepository.getUser("email", email).addOnCompleteListener(task -> {
                                User userToSave = task.getResult();
                                saveOnSharedPreferences(userToSave);
                            });
                        })
                        .addOnFailureListener(f -> {
                            if (f instanceof FirebaseNetworkException)
                                Toast.makeText(this.getContext(), getString(R.string.label_provide_internet_error), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(this.getContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        });
            else
                loginEmail.setError(getString(R.string.label_wrong_email_format_provided));
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                        String idToken = googleSignInAccountTask.getResult().getIdToken();

                        if(idToken != null){
                            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                            firebaseAuth.signInWithCredential(firebaseCredential)
                                    .addOnCompleteListener(myActivity, task -> {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = firebaseAuth.getCurrentUser();

                                            userRepository.getUser("email", user.getEmail()).addOnCompleteListener(task1 -> {
                                                User userLogged = task1.getResult();
                                                if (userLogged != null) {
                                                    String userDisplayName = userLogged.getUsername();
                                                    String welcomeLoginMessage = getString(R.string.label_welcome_back) + " " + userDisplayName;
                                                    Toast.makeText(getContext(), welcomeLoginMessage, Toast.LENGTH_SHORT).show();
                                                    saveOnSharedPreferences(userLogged);
                                                }
                                            });
                                        } else {
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            Toast.makeText(getContext(),getString(R.string.label_provide_internet_error),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.label_provide_internet_error), Toast.LENGTH_SHORT).show();
                    }
                });

        loginWithGoogleButton.setOnClickListener(g -> {
            Intent loginWithGoogleIntent = googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(loginWithGoogleIntent);
        });
    }

    private void saveOnSharedPreferences(User user){
        UserSessionSharedManager.saveUserSession(getContext(), user);
    }
}