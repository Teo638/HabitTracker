package ba.sum.fsre.habittracker.api;

import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.model.ChallengeParticipant;
import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.model.Habit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.PATCH;
import retrofit2.http.Body;

public interface SupabaseDataApi {

    @GET("profiles")
    Call<List<UserProfile>> getUserProfile(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query("id") String userId
    );

    @PATCH("profiles")
    Call<Void> updateProfile(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query("id") String userId,
            @Body UserProfile profile
    );

    @retrofit2.http.GET("challenges")
    retrofit2.Call<java.util.List<Challenge>> getAllChallenges(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("select") String select
    );

    @retrofit2.http.POST("challenges")
    retrofit2.Call<Void> createChallenge(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Body ba.sum.fsre.habittracker.model.Challenge challenge
    );

    @retrofit2.http.POST("challenge_participants")
    retrofit2.Call<Void> joinChallenge(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Body ChallengeParticipant participant
    );

    @retrofit2.http.DELETE("challenge_participants")
    retrofit2.Call<Void> leaveChallenge(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("challenge_id") String challengeId,
            @retrofit2.http.Query("user_id") String userId
    );

    @retrofit2.http.GET("challenge_participants")
    retrofit2.Call<java.util.List<ChallengeParticipant>> checkParticipation(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("challenge_id") String challengeId,
            @retrofit2.http.Query("user_id") String userId
    );
}