package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.repo.ChallengeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChallengesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChallengesAdapter adapter;
    private ChallengeRepository repository;

    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        recyclerView = findViewById(R.id.recyclerChallenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChallengesAdapter();
        recyclerView.setAdapter(adapter);

        repository = new ChallengeRepository(this);
        loadChallenges();

        fabAdd = findViewById(R.id.fabAddChallenge);
        fabAdd.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(ChallengesActivity.this, CreateChallengeActivity.class);
            startActivity(intent);
        });
    }

    private void loadChallenges() {
        repository.getAllChallenges(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setChallenges(response.body());
                } else {
                    Toast.makeText(ChallengesActivity.this, "Greška: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                Toast.makeText(ChallengesActivity.this, "Greška mreže", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadChallenges();
    }
}