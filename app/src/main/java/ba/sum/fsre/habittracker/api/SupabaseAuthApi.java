package ba.sum.fsre.habittracker.api;


import ba.sum.fsre.habittracker.model.LoginRequest;
import ba.sum.fsre.habittracker.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface SupabaseAuthApi {



    @POST
    Call<UserResponse> loginUser(
            @Url String url,
            @Body LoginRequest request,
            @Query("grant_type") String grantType
    );
}