package it.uniba.sms222325;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG_LOG = SplashActivity.class.getName();

    private static final long MIN_WAIT_INTERVAL = 3500L;
    private static final long MAX_WAIT_INTERVAL = 3500L;
    private static final int GO_AHEAD_WHAT = 1;

    private long mStartTime;
    private boolean mIsDone;

    private UiHandler mHandler;

    /**
     * Inner class, it report to garbage collector the istance of splash screen is disposable.
     * It prevents memory leak freak.
     */
    private static class UiHandler extends Handler {
        private final WeakReference<SplashActivity> mActivityRef;
        public UiHandler(final SplashActivity splashActivity){
            this.mActivityRef = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message message){
            final SplashActivity splashActivity = this.mActivityRef.get();
            if (splashActivity == null){
                Log.d(TAG_LOG, "Reference to SplashScreen lost.");
                return;
            }
            if (message.what == GO_AHEAD_WHAT) {
                long elapsedTime = SystemClock.uptimeMillis() - splashActivity.mStartTime;
                if (elapsedTime >= MIN_WAIT_INTERVAL && !splashActivity.mIsDone) {
                    splashActivity.mIsDone = true;
                    splashActivity.goAhead();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStartTime = SystemClock.uptimeMillis();
        final Message goAheadMessage = mHandler.obtainMessage(GO_AHEAD_WHAT);
        mHandler.sendMessageAtTime(goAheadMessage, mStartTime + MAX_WAIT_INTERVAL);
        isUserAlreadyLogged();
        Log.d(TAG_LOG, "Handler message sent!");
    }

    private void goAhead() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        mHandler = new UiHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void isUserAlreadyLogged(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
            Toast.makeText(this, "Benvenuto " + account.getDisplayName(), Toast.LENGTH_SHORT).show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            Toast.makeText(this, "Firebase: " + user.getEmail(), Toast.LENGTH_SHORT).show();
    }
}