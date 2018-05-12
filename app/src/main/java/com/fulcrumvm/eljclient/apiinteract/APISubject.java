package com.fulcrumvm.eljclient.apiinteract;

import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.Subject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APISubject {
    @GET("/api/Subjects/{id}")
    Call<Result<Subject>> GetSubject(@Path("id") String id);


}
