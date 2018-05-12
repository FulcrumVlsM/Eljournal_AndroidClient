package com.fulcrumvm.eljclient.apiinteract;

import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.Semester;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APISemester {
    @GET("/api/Semester/{id}")
    Call<Result<Semester>> GetSemester(@Path("id") String id);

    @GET("api/Semester")
    Call<Result<List<Semester>>> GetSemesters();
}
