package com.fulcrumvm.eljclient.apiinteract;

import android.support.annotation.NonNull;

import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.Student;
import com.fulcrumvm.eljclient.model.StudentFlowSubject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIStudent {
    @GET("/api/Students/user/{id}")
    Call<Result<List<Student>>> GetByUser(@Path("id") @NonNull String userId);

    @GET("api/Students/group/{semesterId}/{groupId}")
    Call<Result<List<Student>>> GetByGroup(@Path("semesterId") @NonNull String semesterId,
                                           @Path("groupId") @NonNull String groupId);

    @GET("/api/Students/flow/{semesterId}")
    Call<Result<List<StudentFlowSubject>>> GetStudentFlowAll(@Path("semesterId") @NonNull String semesterId);

    @GET("/api/Students/flow/{semesterId}")
    Call<Result<List<StudentFlowSubject>>> GetStudentFlowByStudent(@Path("semesterId") String semesterId,
                                                   @Query("student") String studentId);

    @GET("/api/Students/flow/{semesterId}")
    Call<Result<List<StudentFlowSubject>>> GetStudentFlowbySubject(@Path("semesterId") String semesterId,
                                                   @Query("flowSubject") String flowSubjectId);
}
