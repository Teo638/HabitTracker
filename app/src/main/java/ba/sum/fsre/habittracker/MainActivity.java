package ba.sum.fsre.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.habittracker.ui.HabitsFragment;
import ba.sum.fsre.habittracker.ui.LoginActivity;
import ba.sum.fsre.habittracker.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private SessionManager sessionManager;

    private Button btnOpenChallenges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sessionManager = new SessionManager(this);

        btnLogout = findViewById(R.id.btnLogout);

        btnOpenChallenges = findViewById(R.id.btnOpenChallenges);

        btnOpenChallenges.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ba.sum.fsre.habittracker.ui.ChallengesActivity.class);
            startActivity(intent);
        });


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HabitsFragment())
                .commit();


        btnLogout.setOnClickListener(v -> {

            sessionManager.clearSession();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}