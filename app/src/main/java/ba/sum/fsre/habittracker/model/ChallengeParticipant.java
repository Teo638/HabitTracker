package ba.sum.fsre.habittracker.model;
import com.google.gson.annotations.SerializedName;

public class ChallengeParticipant {
    @SerializedName("id")
    private String id;
    @SerializedName("challenge_id")
    private String challengeId;
    @SerializedName("user_id")
    private String userId;

    public ChallengeParticipant(String challengeId, String userId) {
        this.challengeId = challengeId;
        this.userId = userId;
    }
}