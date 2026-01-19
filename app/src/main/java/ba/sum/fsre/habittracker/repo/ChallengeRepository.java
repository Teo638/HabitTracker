package ba.sum.fsre.habittracker.repo;

import android.content.Context;
import java.util.List;
import ba.sum.fsre.habittracker.api.SupabaseClient;
import ba.sum.fsre.habittracker.api.SupabaseDataApi;
import ba.sum.fsre.habittracker.model.ChallengeParticipant;
import ba.sum.fsre.habittracker.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import ba.sum.fsre.habittracker.model.Challenge;
import java.util.HashMap;
import java.util.Map;
import ba.sum.fsre.habittracker.model.ChallengeLeaderboardEntry;

public class ChallengeRepository {
    private final SupabaseDataApi api;
    private final SessionManager sessionManager;

    public ChallengeRepository(Context context) {
        this.api = SupabaseClient.getClient(context).create(SupabaseDataApi.class);
        this.sessionManager = new SessionManager(context);
    }

    public void getAllChallenges(Callback<List<Challenge>> callback) {
        if (sessionManager.getToken() == null) return;
        String token = "Bearer " + sessionManager.getToken();
        api.getAllChallenges(token, SupabaseClient.API_KEY, "*").enqueue(callback);
    }

    public void createChallenge(Challenge challenge, Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        challenge.setCreatorId(sessionManager.getUserId());

        api.createChallenge(token, SupabaseClient.API_KEY, challenge).enqueue(callback);
    }
    public void joinChallenge(String challengeId, Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        String userId = sessionManager.getUserId();
        ChallengeParticipant participant = new ChallengeParticipant(challengeId, userId);
        api.joinChallenge(token, SupabaseClient.API_KEY, participant).enqueue(callback);
    }

    public void leaveChallenge(String challengeId, Callback<Void> callback) {
        String token = "Bearer " + sessionManager.getToken();
        api.leaveChallenge(token, SupabaseClient.API_KEY, "eq." + challengeId, "eq." + sessionManager.getUserId()).enqueue(callback);
    }

    public void checkParticipation(String challengeId, Callback<java.util.List<ChallengeParticipant>> callback) {
        String token = "Bearer " + sessionManager.getToken();
        api.checkParticipation(token, SupabaseClient.API_KEY, "eq." + challengeId, "eq." + sessionManager.getUserId()).enqueue(callback);
    }
    public void getChallengeLeaderboard(String challengeId, Callback<List<ChallengeLeaderboardEntry>> callback) {
        String token = "Bearer " + sessionManager.getToken();


        Map<String, String> params = new HashMap<>();
        params.put("challenge_id_param", challengeId);

        api.getChallengeLeaderboard(token, SupabaseClient.API_KEY, params).enqueue(callback);
    }
}