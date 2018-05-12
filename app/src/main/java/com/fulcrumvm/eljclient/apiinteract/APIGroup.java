package com.fulcrumvm.eljclient.apiinteract;

import com.fulcrumvm.eljclient.model.Group;
import com.fulcrumvm.eljclient.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIGroup {
    @GET("/api/Groups")
    Call<Result<List<Group>>> GetGroups(@Query("name") String name);

    @GET("/api/Groups/{id}")
    Call<Result<Group>> GetGroup(@Path("id") String id);

    @GET("/api/Groups/BySemester/{id}")
    Call<Result<List<Group>>> GetGroupsBySemester(@Path("id") String semesterId);
}
