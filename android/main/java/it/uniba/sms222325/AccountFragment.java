package it.uniba.sms222325;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class AccountFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private SignInButton loginWithGoogleButton;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);
        loginButton = view.findViewById(R.id.loginButton);
        loginWithGoogleButton = view.findViewById(R.id.sign_in_button);

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (email.isEmpty()) {
                loginEmail.setError(getString(R.string.no_email_provided));
            } else if (password.isEmpty()) {
                loginEmail.setError(getString(R.string.no_password_provided));
            } else {
                if (Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), email)) {
                    firebaseAuth
                            .signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(s -> Toast.makeText(this.getContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(f -> Toast.makeText(this.getContext(), getString(R.string.Login_failed), Toast.LENGTH_SHORT).show());
                } else {
                    loginEmail.setError(getString(R.string.wrong_email_format_provided));
                }
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

                        try {
                            googleSignInAccountTask.getResult(ApiException.class);
                        } catch (ApiException e){
                            Toast.makeText(getContext(), "Login failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        loginWithGoogleButton.setOnClickListener(g -> {
            Intent loginWithGoogleIntent = googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(loginWithGoogleIntent);
        });

        return view;
    }
}