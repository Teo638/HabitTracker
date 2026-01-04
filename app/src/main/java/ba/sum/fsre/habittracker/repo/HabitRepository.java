package ba.sum.fsre.habittracker.repo;

import com.google.gson.Gson;

import ba.sum.fsre.habittracker.model.Habit;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HabitRepository {

    private static final String SUPABASE_URL = "https://jyzdhvwfgpdtykwpexvt.supabase.co/rest/v1/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imp5emRodndmZ3BkdHlrd3BleHZ0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njc0Njc5MDcsImV4cCI6MjA4MzA0MzkwN30.unmtlMBxAarYKWzTiwLM7P4c8pyLTQOz8s13seTv6so";

    private static final String HABITS_ENDPOINT = "/rest/v1/habits";

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();


    public void getMyHabits(Callback callback) {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + HABITS_ENDPOINT + "?select=*")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        client.newCall(request).enqueue(callback);
    }


    public void createHabit(Habit habit, Callback callback) {

        RequestBody body = RequestBody.create(
                gson.toJson(habit),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(SUPABASE_URL + HABITS_ENDPOINT)
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Prefer", "return=minimal")
                .build();

        client.newCall(request).enqueue(callback);
    }
}
