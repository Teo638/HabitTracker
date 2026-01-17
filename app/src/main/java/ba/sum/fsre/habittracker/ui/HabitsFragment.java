package ba.sum.fsre.habittracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.repo.HabitRepository;



public class HabitsFragment extends Fragment {

    private HabitsAdapter adapter;
    private HabitRepository repository;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        repository = new HabitRepository(requireContext());


        RecyclerView recyclerView = view.findViewById(R.id.recyclerHabits);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new HabitsAdapter(habit -> deleteHabit(habit));
        recyclerView.setAdapter(adapter);


        loadHabits();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHabits();
    }

    private void loadHabits() {

        repository.getMyHabits(new retrofit2.Callback<List<Habit>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Habit>> call,
                                   retrofit2.Response<List<Habit>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    adapter.setHabits(response.body());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Habit>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteHabit(Habit habit) {

        repository.deleteHabit(habit.getId(), new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call,
                                   retrofit2.Response<Void> response) {

                if (response.isSuccessful()) {
                    loadHabits();
                    android.widget.Toast.makeText(
                            getContext(),
                            "Navika obrisana",
                            android.widget.Toast.LENGTH_SHORT
                    ).show();
                } else {
                    android.widget.Toast.makeText(
                            getContext(),
                            "Greška: " + response.code(),
                            android.widget.Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
