package ba.sum.fsre.habittracker.ui;

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
    private HabitRepository repository = new HabitRepository();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_habits, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerHabits);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new HabitsAdapter();
        recyclerView.setAdapter(adapter);


        loadHabits();

        return view;
    }

    /**
    private void loadHabits() {

        repository.getMyHabits(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Type type = new TypeToken<List<Habit>>() {}.getType();
                    List<Habit> habits = new Gson().fromJson(json, type);


                    requireActivity().runOnUiThread(() -> adapter.setHabits(habits));
                }
            }
        });
    }
     **/


private void loadHabits() {

    List<Habit> testHabits = new ArrayList<>();
    testHabits.add(new Habit("Popiti vodu"));
    testHabits.add(new Habit("Vježbati 30 minuta"));
    testHabits.add(new Habit("Nazvati mamu"));
    testHabits.add(new Habit("Pročitati 10 stranica knjige"));
    testHabits.add(new Habit("Pročitati 10 stranica knjige i nastaviti dalje čitati dok ne zaspem"));
    testHabits.add(new Habit("Pročitati 100 stranica knjige"));

    adapter.setHabits(testHabits);
}



}
