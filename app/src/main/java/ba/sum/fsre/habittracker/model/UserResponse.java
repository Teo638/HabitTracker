package ba.sum.fsre.habittracker.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {


    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("user")
    private User user;

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserId() {
        return user != null ? user.id : null;
    }


    private static class User {
        @SerializedName("id")
        String id;

        @SerializedName("email")
        String email;
    }
}