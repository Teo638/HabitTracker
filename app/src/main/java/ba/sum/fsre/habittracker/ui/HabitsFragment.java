package ba.sum.fsre.habittracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.model.HabitLog;
import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.repo.HabitRepository;
import ba.sum.fsre.habittracker.repo.UserProfileRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabitsFragment extends Fragment {

    private HabitsAdapter adapter;
    private HabitRepository habitRepository;
    private UserProfileRepository userProfileRepository;

    private LocalDate today;
    private LocalDate weekStart;
    private LocalDate weekEnd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        habitRepository = new HabitRepository(requireContext());
        userProfileRepository = new UserProfileRepository(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerHabits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HabitsAdapter(new HabitsAdapter.OnHabitActionListener() {
            @Override
            public void onDelete(Habit habit) {
                deleteHabit(habit);
            }

            @Override
            public void onCheckIn(Habit habit) {
                checkIn(habit);
            }
        });

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddHabit);
        fab.setOnClickListener(v -> startActivity(new Intent(getContext(), AddHabitActivity.class)));

        loadHabitsAndWeeklyLogs();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHabitsAndWeeklyLogs();
    }

    private void loadHabitsAndWeeklyLogs() {
        today = LocalDate.now();
        weekStart = today.with(DayOfWeek.MONDAY);
        weekEnd = weekStart.plusDays(6);

        adapter.setWeek(weekStart, today);

        habitRepository.getMyHabits(new Callback<List<Habit>>() {
            @Override
            public void onResponse(Call<List<Habit>> call, Response<List<Habit>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Habit> habits = response.body();
                    adapter.setHabits(habits);

                    List<String> habitIds = new ArrayList<>();
                    for (Habit h : habits) habitIds.add(h.getId());

                    loadWeeklyLogs(habitIds);
                }
            }

            @Override
            public void onFailure(Call<List<Habit>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadWeeklyLogs(List<String> habitIds) {
        habitRepository.getWeeklyLogs(habitIds, weekStart.toString(), weekEnd.toString(),
                new Callback<List<HabitLog>>() {
                    @Override
                    public void onResponse(Call<List<HabitLog>> call, Response<List<HabitLog>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            Map<String, java.util.Set<String>> map = new HashMap<>();

                            for (HabitLog log : response.body()) {
                                String hid = log.getHabitId();
                                if (!map.containsKey(hid)) map.put(hid, new HashSet<>());


                                map.get(hid).add(log.getCompletedAt());
                            }

                            adapter.setWeeklyDone(map);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<HabitLog>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void checkIn(Habit habit) {
        habitRepository.logHabitDone(habit.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {


                    userProfileRepository.getMyProfile(new Callback<List<UserProfile>>() {
                        @Override
                        public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                                UserProfile profile = response.body().get(0);
                                profile.setPoints(profile.getPoints() + 10);
                                userProfileRepository.updateMyProfile(profile, new Callback<Void>() {
                                    @Override public void onResponse(Call<Void> c, Response<Void> r) {}
                                    @Override public void onFailure(Call<Void> c, Throwable t) {}
                                });
                            }
                        }
                        @Override public void onFailure(Call<List<UserProfile>> call, Throwable t) {}
                    });


                    loadHabitsAndWeeklyLogs();

                } else {
                    Toast.makeText(getContext(), "Greška check-in: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteHabit(Habit habit) {
        habitRepository.deleteHabit(habit.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadHabitsAndWeeklyLogs();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
