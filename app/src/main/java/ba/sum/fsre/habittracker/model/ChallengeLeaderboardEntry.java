package ba.sum.fsre.habittracker.model;

import com.google.gson.annotations.SerializedName;

public class ChallengeLeaderboardEntry {
    @SerializedName("username")
    private String username;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("score")
    private long score;

    public String getUsername() { return username; }
    public String getAvatarUrl() { return avatarUrl; }
    public long getScore() { return score; }
}