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

    private java.util.Set<String> completedToday = new java.util.HashSet<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        repository = new HabitRepository(requireContext());


        RecyclerView recyclerView = view.findViewById(R.id.recyclerHabits);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new HabitsAdapter(new HabitsAdapter.OnHabitActionListener() {
            @Override public void onDelete(Habit habit) { deleteHabit(habit); }

            @Override public void onCheck(Habit habit, boolean isChecked) {
                toggleLog(habit, isChecked);
            }
        });

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
                    loadTodayLogs();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Habit>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadTodayLogs() {
        repository.getTodayLogs(new retrofit2.Callback<List<ba.sum.fsre.habittracker.model.HabitLog>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ba.sum.fsre.habittracker.model.HabitLog>> call,
                                   retrofit2.Response<List<ba.sum.fsre.habittracker.model.HabitLog>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    java.util.Set<String> ids = new java.util.HashSet<>();
                    for (ba.sum.fsre.habittracker.model.HabitLog log : response.body()) {
                        ids.add(log.getHabitId());
                    }
                    completedToday = ids;
                    adapter.setCompletedToday(completedToday);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<ba.sum.fsre.habittracker.model.HabitLog>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void toggleLog(Habit habit, boolean isChecked) {
        if (isChecked) {
            repository.logHabitDone(habit.getId(), new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        completedToday.add(habit.getId());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            repository.unlogHabitDone(habit.getId(), new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        completedToday.remove(habit.getId());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
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
