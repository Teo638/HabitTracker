package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.model.ChallengeLeaderboardEntry;
import ba.sum.fsre.habittracker.model.ChallengeParticipant;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.repo.ChallengeRepository;
import ba.sum.fsre.habittracker.repo.HabitRepository;
import ba.sum.fsre.habittracker.repo.UserProfileRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengeDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvDesc, tvDate;
    private Button btnJoinLeave;
    private RecyclerView recyclerParticipants;
    private LeaderboardAdapter participantsAdapter;
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

        recyclerParticipants = findViewById(R.id.recyclerParticipants);
        recyclerParticipants.setLayoutManager(new LinearLayoutManager(this));
        participantsAdapter = new LeaderboardAdapter();
        recyclerParticipants.setAdapter(participantsAdapter);

        repository = new ChallengeRepository(this);
        habitRepository = new HabitRepository(this);
        userProfileRepository = new UserProfileRepository(this);


        challenge = (Challenge) getIntent().getSerializableExtra("challenge_data");

        if (challenge != null) {
            tvTitle.setText(challenge.getTitle());
            tvDesc.setText(challenge.getDescription());
            tvDate.setText("Početak: " + challenge.getStartDate());
            checkStatus();

            loadParticipants();
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

        newHabit.setChallengeId(challenge.getId());

        habitRepository.createHabit(newHabit, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                loadParticipants();
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

    private void loadParticipants() {
        repository.getChallengeLeaderboard(challenge.getId(), new Callback<List<ChallengeLeaderboardEntry>>() {
            @Override
            public void onResponse(Call<List<ChallengeLeaderboardEntry>> call, Response<List<ChallengeLeaderboardEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<UserProfile> profiles = new ArrayList<>();
                    for (ChallengeLeaderboardEntry entry : response.body()) {
                        UserProfile p = new UserProfile();
                        p.setUsername(entry.getUsername());
                        p.setAvatarUrl(entry.getAvatarUrl());
                        p.setPoints(entry.getScore());
                        profiles.add(p);
                    }
                    participantsAdapter.setUsers(profiles);
                }
            }
            @Override public void onFailure(Call<List<ChallengeLeaderboardEntry>> call, Throwable t) {}
        });
    }

    private void performLeave() {
        repository.leaveChallenge(challenge.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChallengeDetailsActivity.this, "Napušteno!", Toast.LENGTH_SHORT).show();
                    isJoined = false;
                    updateButtonUI();
                    cleanupAfterLeave();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
    private void cleanupAfterLeave() {

        userProfileRepository.addPoints(-50, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> c, Response<Void> r) {
            }

            @Override
            public void onFailure(Call<Void> c, Throwable t) {
            }
        });


        habitRepository.deleteHabitByChallengeId(challenge.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loadParticipants();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}