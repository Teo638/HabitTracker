package ba.sum.fsre.habittracker;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.ui.ChallengesFragment;
import ba.sum.fsre.habittracker.ui.HabitsFragment;
import ba.sum.fsre.habittracker.ui.LeaderboardFragment;
import ba.sum.fsre.habittracker.ui.LoginActivity;
import ba.sum.fsre.habittracker.utils.NotificationScheduler;
import ba.sum.fsre.habittracker.utils.SessionManager;
import ba.sum.fsre.habittracker.ui.EditProfileActivity;
import ba.sum.fsre.habittracker.repo.UserProfileRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private UserProfileRepository userProfileRepository;
    private ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        userProfileRepository = new UserProfileRepository(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        imgProfile = findViewById(R.id.imgProfile);

        loadUserProfile();


        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });


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
    private void loadUserProfile() {
        userProfileRepository.getMyProfile(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    UserProfile profile = response.body().get(0);
                    String avatarUrl = profile.getAvatarUrl();


                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Picasso.get()
                                .load(avatarUrl)
                                .placeholder(R.drawable.ic_default_profile)
                                .error(R.drawable.ic_default_profile)
                                .into(imgProfile);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }
}
