package ba.sum.fsre.habittracker.api;

import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.model.UserProfile;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SupabaseDataApi {

    @GET("profiles")
    Call<List<UserProfile>> getUserProfile(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query("id") String userId
    );

    @retrofit2.http.GET("challenges")
    retrofit2.Call<java.util.List<Challenge>> getAllChallenges(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("select") String select
    );
}