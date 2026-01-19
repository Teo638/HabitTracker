package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ba.sum.fsre.habittracker.R;

import android.content.Intent;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.repo.ChallengeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChallengesAdapter adapter;
    private ChallengeRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_challenges, container, false);


        recyclerView = view.findViewById(R.id.recyclerChallenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChallengesAdapter();
        recyclerView.setAdapter(adapter);


        repository = new ChallengeRepository(requireContext());


        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddChallenge);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateChallengeActivity.class);
            startActivity(intent);
        });


        loadChallenges();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadChallenges();
    }


    private void loadChallenges() {
        repository.getAllChallenges(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setChallenges(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Greška mreže", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}