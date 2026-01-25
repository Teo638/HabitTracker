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
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserProfileRepository {
    private final SupabaseDataApi api;
    private final SessionManager sessionManager;
    private final String API_KEY = SupabaseClient.API_KEY;

    public UserProfileRepository(Context context) {
        this.api = SupabaseClient.getClient(context).create(SupabaseDataApi.class);
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

    public void uploadAvatar(byte[] imageBytes, final Callback<String> callback) {
        String userId = sessionManager.getUserId();
        String fileName = userId + ".jpg";


        String uploadUrl = SupabaseClient.BASE_URL.replace("/rest/v1/", "/storage/v1/object/avatars/" + fileName);


        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        String token = "Bearer " + sessionManager.getToken();


        api.uploadImage(uploadUrl, token, API_KEY, "image/jpeg", requestBody).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    String publicUrl = SupabaseClient.BASE_URL.replace("/rest/v1/", "/storage/v1/object/public/avatars/" + fileName);


                    callback.onResponse(null, Response.success(publicUrl));
                } else {

                    callback.onFailure(null, new Throwable("Upload failed: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                callback.onFailure(null, t);
            }
        });
    }

    public void getLeaderboard(final Callback<List<UserProfile>> callback) {
        String token = "Bearer " + sessionManager.getToken();
        api.getLeaderboard(token, SupabaseClient.API_KEY, "*", "points.desc").enqueue(callback);
    }

    public void addPoints(int delta, final Callback<Void> callback) {
        getMyProfile(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    UserProfile me = response.body().get(0);
                    me.setPoints(me.getPoints() + delta);
                    updateMyProfile(me, callback);
                } else {
                    callback.onFailure(null, new Throwable("Profile not found"));
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });
    }
    public void deleteUserAccount(final Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        String queryId = "eq." + sessionManager.getUserId();

        api.deleteProfile(token, API_KEY, queryId).enqueue(callback);
    }

}
