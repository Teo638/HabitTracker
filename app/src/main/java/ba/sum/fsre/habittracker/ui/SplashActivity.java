package ba.sum.fsre.habittracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.habittracker.MainActivity;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            SessionManager sessionManager = new SessionManager(this);

            if (sessionManager.isLoggedIn()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            finish();

        }, 2000);
    }
}