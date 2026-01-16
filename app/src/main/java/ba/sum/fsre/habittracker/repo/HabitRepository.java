package ba.sum.fsre.habittracker.repo;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import ba.sum.fsre.habittracker.api.SupabaseClient;
import ba.sum.fsre.habittracker.api.SupabaseDataApi;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.utils.SessionManager;
import retrofit2.Callback;


public class HabitRepository {

    private final SupabaseDataApi api;
    private final SessionManager sessionManager;

    public HabitRepository(Context context) {
        this.api = SupabaseClient.getClient().create(SupabaseDataApi.class);
        this.sessionManager = new SessionManager(context);
    }

    public void getMyHabits(Callback<List<Habit>> callback) {
        String token = "Bearer " + sessionManager.getToken();
        api.getHabits(token, SupabaseClient.API_KEY, "*")
                .enqueue(callback);
    }

    public void createHabit(Habit habit, Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();


        habit.setUserId(sessionManager.getUserId());

        api.createHabit(token, SupabaseClient.API_KEY, habit)
                .enqueue(callback);
    }

    public void deleteHabit(String habitId, Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        api.deleteHabit(token, SupabaseClient.API_KEY, "eq." + habitId)
                .enqueue(callback);
    }
}
