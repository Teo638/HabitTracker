package ba.sum.fsre.habittracker.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {


    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("user")
    private User user;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() { return refreshToken; }

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