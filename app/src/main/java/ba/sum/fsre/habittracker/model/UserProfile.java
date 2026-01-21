package ba.sum.fsre.habittracker.model;
import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("id")
    private String id;


    @SerializedName("username")
    private String username;

    @SerializedName("points")
    private long points;

    @SerializedName("bio")
    private String bio;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("accepted_terms")
    private boolean acceptedTerms;

    public UserProfile() {}

    public UserProfile(String id, String username, long points, String bio) {
        this.id = id;
        this.username = username;
        this.points = points;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }
}


