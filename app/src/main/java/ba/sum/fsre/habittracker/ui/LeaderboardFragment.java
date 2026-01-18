package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.repo.UserProfileRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private UserProfileRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);


        recyclerView = view.findViewById(R.id.recyclerLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LeaderboardAdapter();
        recyclerView.setAdapter(adapter);


        repository = new UserProfileRepository(requireContext());


        loadLeaderboard();

        return view;
    }

    private void loadLeaderboard() {
        repository.getLeaderboard(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setUsers(response.body());
                } else {
                    Toast.makeText(getContext(), "Greška: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                Toast.makeText(getContext(), "Greška mreže", Toast.LENGTH_SHORT).show();
            }
        });
    }
}