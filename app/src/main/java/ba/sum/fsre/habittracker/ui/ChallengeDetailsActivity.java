package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.model.ChallengeParticipant;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.repo.ChallengeRepository;
import ba.sum.fsre.habittracker.repo.HabitRepository;
import ba.sum.fsre.habittracker.repo.UserProfileRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengeDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvDesc, tvDate;
    private Button btnJoinLeave;
    private ChallengeRepository repository;
    private Challenge challenge;
    private boolean isJoined = false;

    private HabitRepository habitRepository;
    private UserProfileRepository userProfileRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDesc = findViewById(R.id.tvDetailDesc);
        tvDate = findViewById(R.id.tvDetailDate);
        btnJoinLeave = findViewById(R.id.btnJoinLeave);
        repository = new ChallengeRepository(this);

        habitRepository = new HabitRepository(this);
        userProfileRepository = new UserProfileRepository(this);


        challenge = (Challenge) getIntent().getSerializableExtra("challenge_data");

        if (challenge != null) {
            tvTitle.setText(challenge.getTitle());
            tvDesc.setText(challenge.getDescription());
            tvDate.setText("Početak: " + challenge.getStartDate());
            checkStatus();
        }

        btnJoinLeave.setOnClickListener(v -> {
            if (isJoined) performLeave();
            else performJoin();
        });
    }

    private void checkStatus() {
        repository.checkParticipation(challenge.getId(), new Callback<List<ChallengeParticipant>>() {
            @Override
            public void onResponse(Call<List<ChallengeParticipant>> call, Response<List<ChallengeParticipant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isJoined = !response.body().isEmpty();
                    updateButtonUI();
                }
            }
            @Override
            public void onFailure(Call<List<ChallengeParticipant>> call, Throwable t) {}
        });
    }

    private void updateButtonUI() {
        btnJoinLeave.setEnabled(true);
        if (isJoined) {
            btnJoinLeave.setText("Napusti Izazov");
            btnJoinLeave.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            btnJoinLeave.setText("Pridruži se");
            btnJoinLeave.setBackgroundColor(getResources().getColor(R.color.blue));
        }
    }

    private void performJoin() {
        repository.joinChallenge(challenge.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChallengeDetailsActivity.this, "Pridruženo!", Toast.LENGTH_SHORT).show();
                    isJoined = true;
                    updateButtonUI();

                    createLinkedHabitAndAddPoints();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
    private void createLinkedHabitAndAddPoints() {

        String habitTitle = "🏆 " + challenge.getTitle();
        Habit newHabit = new Habit(null, habitTitle, "Izazov: " + challenge.getTitle(), "Daily");

        habitRepository.createHabit(newHabit, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {}
        });

        userProfileRepository.addPoints(50, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
        Toast.makeText(this, "Osvojeno 50 XP!", Toast.LENGTH_SHORT).show();
    }

    private void performLeave() {
        repository.leaveChallenge(challenge.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChallengeDetailsActivity.this, "Napušteno!", Toast.LENGTH_SHORT).show();
                    isJoined = false;
                    updateButtonUI();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}