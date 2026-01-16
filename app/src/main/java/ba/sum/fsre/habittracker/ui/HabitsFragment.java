package ba.sum.fsre.habittracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.repo.HabitRepository;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HabitsFragment extends Fragment {

    private HabitsAdapter adapter;
    private HabitRepository repository;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        repository = new HabitRepository(requireContext());


        RecyclerView recyclerView = view.findViewById(R.id.recyclerHabits);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new HabitsAdapter();
        recyclerView.setAdapter(adapter);


        loadHabits();

        return view;
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

}
