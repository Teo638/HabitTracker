package ba.sum.fsre.habittracker;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import ba.sum.fsre.habittracker.ui.ChallengesFragment;
import ba.sum.fsre.habittracker.ui.HabitsFragment;
import ba.sum.fsre.habittracker.ui.LeaderboardFragment;
import ba.sum.fsre.habittracker.ui.LoginActivity;
import ba.sum.fsre.habittracker.utils.NotificationScheduler;
import ba.sum.fsre.habittracker.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HabitsFragment())
                    .commit();
        }


        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HabitsFragment();
            } else if (id == R.id.nav_challenges) {
                selectedFragment = new ChallengesFragment();
            } else if (id == R.id.nav_leaderboard) {
                selectedFragment = new LeaderboardFragment();
            } else if (id == R.id.nav_logout) {

                sessionManager.clearSession();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }
            return true;
        });


        NotificationScheduler.scheduleNotifications(this);
    }
}