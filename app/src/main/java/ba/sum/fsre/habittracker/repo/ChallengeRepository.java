package ba.sum.fsre.habittracker.repo;

import android.content.Context;
import java.util.List;
import ba.sum.fsre.habittracker.api.SupabaseClient;
import ba.sum.fsre.habittracker.api.SupabaseDataApi;
import ba.sum.fsre.habittracker.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import ba.sum.fsre.habittracker.model.Challenge;

public class ChallengeRepository {
    private final SupabaseDataApi api;
    private final SessionManager sessionManager;

    public ChallengeRepository(Context context) {
        this.api = SupabaseClient.getClient().create(SupabaseDataApi.class);
        this.sessionManager = new SessionManager(context);
    }

    public void getAllChallenges(Callback<List<Challenge>> callback) {
        if (sessionManager.getToken() == null) return;
        String token = "Bearer " + sessionManager.getToken();
        api.getAllChallenges(token, SupabaseClient.API_KEY, "*").enqueue(callback);
    }
}