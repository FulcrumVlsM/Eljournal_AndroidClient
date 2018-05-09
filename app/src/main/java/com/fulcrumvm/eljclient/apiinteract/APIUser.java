package com.fulcrumvm.eljclient.apiinteract;

import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIUser {
    @GET("/api/People")
    Call<Result<List<User>>> GetUsers(@Query("name") String name, @Query("roleId") String role);

    @GET("/api/People")
    Call<Result<List<User>>> GetUsers(@Query("name") String name, @Query("roleId") String role,
                                      @Query("count") int count);

    @GET("/api/People/{id}")
    Call<Result<User>> GetUser(@Path("id") String userId);

    @GET("/api/People/{id}")
    Call<Result<User>> GetUser(@Path("id") String userId, @Header("Authorization") String token);
}
