package ba.sum.fsre.habittracker.api;

import android.content.Context;
import androidx.annotation.Nullable;
import java.io.IOException;
import ba.sum.fsre.habittracker.utils.SessionManager;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import org.json.JSONObject;

public class TokenAuthenticator implements Authenticator {

    private Context context;
    private SessionManager sessionManager;

    public TokenAuthenticator(Context context) {
        this.context = context;
        this.sessionManager = new SessionManager(context);
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {

        if (responseCount(response) >= 2) {
            return null;
        }

        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken == null) {
            return null;
        }


        OkHttpClient client = new OkHttpClient();


        String json = "{\"refresh_token\": \"" + refreshToken + "\"}";
        RequestBody body = RequestBody.create(json, okhttp3.MediaType.parse("application/json"));


        String refreshUrl = SupabaseClient.BASE_URL.replace("/rest/v1/", "/auth/v1/token?grant_type=refresh_token");

        Request refreshRequest = new Request.Builder()
                .url(refreshUrl)
                .post(body)
                .header("apikey", SupabaseClient.API_KEY)
                .header("Content-Type", "application/json")
                .build();

        try {

            Response refreshResponse = client.newCall(refreshRequest).execute();

            if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {

                String responseString = refreshResponse.body().string();
                JSONObject jsonObject = new JSONObject(responseString);

                String newAccessToken = jsonObject.getString("access_token");
                String newRefreshToken = jsonObject.getString("refresh_token");


                sessionManager.saveSession(newAccessToken, newRefreshToken, sessionManager.getUserId());


                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + newAccessToken)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}