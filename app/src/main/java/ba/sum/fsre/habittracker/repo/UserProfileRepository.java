package ba.sum.fsre.habittracker.repo;

import android.content.Context;
import java.util.List;
import ba.sum.fsre.habittracker.api.SupabaseClient;
import ba.sum.fsre.habittracker.api.SupabaseDataApi;
import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileRepository {
    private final SupabaseDataApi api;
    private final SessionManager sessionManager;
    private final String API_KEY = SupabaseClient.API_KEY;

    public UserProfileRepository(Context context) {
        this.api = SupabaseClient.getClient().create(SupabaseDataApi.class);
        this.sessionManager = new SessionManager(context);
    }


    public void getMyProfile(final Callback<List<UserProfile>> callback) {
        String token = "Bearer " + sessionManager.getToken();
        String queryId = "eq." + sessionManager.getUserId();

        api.getUserProfile(token, API_KEY, queryId).enqueue(callback);
    }


    public void updateMyProfile(UserProfile profile, final Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        String queryId = "eq." + sessionManager.getUserId();

        api.updateProfile(token, API_KEY, queryId, profile).enqueue(callback);
    }
}