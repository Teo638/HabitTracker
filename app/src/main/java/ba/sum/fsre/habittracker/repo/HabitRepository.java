package ba.sum.fsre.habittracker.repo;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import ba.sum.fsre.habittracker.api.SupabaseClient;
import ba.sum.fsre.habittracker.api.SupabaseDataApi;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.model.HabitLog;
import ba.sum.fsre.habittracker.utils.SessionManager;
import retrofit2.Callback;


public class HabitRepository {

    private final SupabaseDataApi api;
    private final SessionManager sessionManager;

    public HabitRepository(Context context) {
        this.api = SupabaseClient.getClient(context).create(SupabaseDataApi.class);
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

    private String today() {
        return java.time.LocalDate.now().toString(); // "YYYY-MM-DD"
    }

    public void getTodayLogs(retrofit2.Callback<List<HabitLog>> callback) {
        String token = "Bearer " + sessionManager.getToken();
        api.getTodayLogs(token, SupabaseClient.API_KEY, "habit_id", "eq." + today())
                .enqueue(callback);
    }

    public void logHabitDone(String habitId, retrofit2.Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        HabitLog log = new HabitLog(habitId, sessionManager.getUserId(), today());
        api.createHabitLog(token, SupabaseClient.API_KEY, log).enqueue(callback);
    }

    public void unlogHabitDone(String habitId, retrofit2.Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        api.deleteHabitLog(token, SupabaseClient.API_KEY,
                "eq." + habitId,
                "eq." + today()
        ).enqueue(callback);
    }

}
