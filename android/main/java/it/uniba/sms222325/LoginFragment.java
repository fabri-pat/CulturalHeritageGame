package it.uniba.sms222325;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.regex.Pattern;
import it.uniba.sms222325.repositories.UserRepository;

public class LoginFragment extends Fragment {
    private GoogleSignInClient googleSignInClient;
    private FragmentActivity myActivity;

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

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        EditText loginEmail = view.findViewById(R.id.loginEmail);
        EditText loginPassword = view.findViewById(R.id.loginPassword);
        Button loginButton = view.findViewById(R.id.loginButton);
        SignInButton loginWithGoogleButton = view.findViewById(R.id.sign_in_button);
        View registerPageText = view.findViewById(R.id.registerTextView);

        registerPageText.setOnClickListener(x -> {
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
                    loginEmail.setError(getString(R.string.no_email_provided));
                if (password.isEmpty())
                    loginPassword.setError(getString(R.string.no_password_provided));
                return;
            }

            if (Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), email))
                firebaseAuth
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(s -> Toast.makeText(this.getContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(f -> Toast.makeText(this.getContext(), getString(R.string.Login_failed), Toast.LENGTH_SHORT).show());
            else
                loginEmail.setError(getString(R.string.wrong_email_format_provided));
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

                                            UserRepository.getInstance().getUser("email", user.getEmail()).addOnCompleteListener(task1 -> {
                                                if (task1.getResult() != null) {
                                                    String userDisplayName = task1.getResult().getUsername();
                                                    String welcomeLoginMessage = getString(R.string.welcomeMessageLogin) + " " + userDisplayName;
                                                    Toast.makeText(getContext(), welcomeLoginMessage, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            Toast.makeText(getContext(),getString(R.string.googleLoginFailure),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

        loginWithGoogleButton.setOnClickListener(g -> {
            Intent loginWithGoogleIntent = googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(loginWithGoogleIntent);
        });

        return view;
    }


}