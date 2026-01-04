package ba.sum.fsre.habittracker.model;
import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("id")
    public String id;


    @SerializedName("username")
    public String username;
}