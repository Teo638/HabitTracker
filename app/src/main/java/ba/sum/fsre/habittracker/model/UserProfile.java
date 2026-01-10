package ba.sum.fsre.habittracker.model;
import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("id")
    private String id;


    @SerializedName("username")
    private String username;

    @SerializedName("points")
    private int points;

    @SerializedName("bio")
    private String bio;

    public UserProfile() {}

    public UserProfile(String id, String username, int points, String bio) {
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}


