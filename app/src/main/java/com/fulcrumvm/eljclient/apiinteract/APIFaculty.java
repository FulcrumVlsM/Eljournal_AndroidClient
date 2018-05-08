package com.fulcrumvm.eljclient.apiinteract;

import com.fulcrumvm.eljclient.model.Faculty;
import com.fulcrumvm.eljclient.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIFaculty {
    @GET("/api/Faculties/{id}")
    Call<Result<Faculty>> GetFaculty(@Path("id") String facultyId);

    @GET("/api/Faculties")
    Call<Result<List<Faculty>>> GetFaculties();
}
