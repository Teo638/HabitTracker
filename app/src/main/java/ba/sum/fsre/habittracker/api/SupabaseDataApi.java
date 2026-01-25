package ba.sum.fsre.habittracker.api;

import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.model.ChallengeLeaderboardEntry;
import ba.sum.fsre.habittracker.model.ChallengeParticipant;
import ba.sum.fsre.habittracker.model.HabitLog;
import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.model.Habit;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.PATCH;
import retrofit2.http.Body;
import retrofit2.http.Url;

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

    @DELETE("profiles")
    Call<Void> deleteProfile(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query("id") String queryId
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


    @retrofit2.http.GET("habits")
    retrofit2.Call<java.util.List<Habit>> getHabits(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("select") String select
    );

    @retrofit2.http.POST("habits")
    retrofit2.Call<Void> createHabit(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Body Habit habit
    );

    @retrofit2.http.DELETE("habits")
    retrofit2.Call<Void> deleteHabit(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("id") String habitIdFilter
    );


    @retrofit2.http.GET("habit_logs")
    retrofit2.Call<java.util.List<HabitLog>> getTodayLogs(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("select") String select,
            @retrofit2.http.Query("completed_at") String completedAtFilter
    );

    @retrofit2.http.POST("habit_logs")
    retrofit2.Call<Void> createHabitLog(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Body HabitLog log
    );

    @retrofit2.http.DELETE("habit_logs")
    retrofit2.Call<Void> deleteHabitLog(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("habit_id") String habitIdFilter,
            @retrofit2.http.Query("completed_at") String completedAtFilter
    );

    @retrofit2.http.Headers("x-upsert: true")
    @POST
    Call<Void> uploadImage(
            @Url String fullUrl,
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Header("Content-Type") String contentType,
            @Body RequestBody image
    );

    @GET("profiles")
    Call<List<UserProfile>> getLeaderboard(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Query("select") String select,
            @Query("order") String order
    );

    @retrofit2.http.GET
    retrofit2.Call<java.util.List<HabitLog>> getLogsByUrl(
            @retrofit2.http.Url String fullUrl,
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey
    );

    @POST("rpc/get_challenge_leaderboard")
    Call<List<ChallengeLeaderboardEntry>> getChallengeLeaderboard(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Body java.util.Map<String, String> body
    );

    @retrofit2.http.DELETE("habits")
    retrofit2.Call<Void> deleteHabitByChallenge(
            @retrofit2.http.Header("Authorization") String token,
            @retrofit2.http.Header("apikey") String apiKey,
            @retrofit2.http.Query("challenge_id") String challengeId,
            @retrofit2.http.Query("user_id") String userId
    );




}