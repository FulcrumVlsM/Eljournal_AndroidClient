package com.fulcrumvm.eljclient.model.advanced;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fulcrumvm.eljclient.apiinteract.APIGroup;
import com.fulcrumvm.eljclient.apiinteract.APISemester;
import com.fulcrumvm.eljclient.apiinteract.APIStudent;
import com.fulcrumvm.eljclient.model.Group;
import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.Semester;
import com.fulcrumvm.eljclient.model.Student;
import com.fulcrumvm.eljclient.model.StudentFlowSubject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentInfoSimple extends Student {
    public Group group;
    public Semester semester;
    public int subjectsCount;

    public StudentInfoSimple(Student student, @NonNull String apiUrl){
        this.ID = student.ID;
        this.PersonId = student.PersonId;
        this.GroupId = student.GroupId;
        this.SemesterId = student.SemesterId;


        Retrofit retrofit = new Retrofit.Builder().baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //получение группы
        APIGroup groupService = retrofit.create(APIGroup.class);
        try{
            Response<Result<Group>> response1 = groupService.GetGroup(GroupId).execute();
            if(response1.isSuccessful())
                group = response1.body().Data;
            else
                group = new Group();
        }
        catch(IOException e){
            group = new Group();
            Log.e("StudentInfoSimple",e.getMessage());
        }

        //получение семестра
        APISemester semesterService = retrofit.create(APISemester.class);
        try{
            Response<Result<Semester>> response2 = semesterService.GetSemester(SemesterId).execute();
            if(response2.isSuccessful())
                semester = response2.body().Data;
            else
                semester = new Semester();
        }
        catch(IOException e){
            semester = new Semester();
            Log.e("StudentInfoSimple",e.getMessage());
        }

        //полчение количества изучаемых предметов
        APIStudent studentService = retrofit.create(APIStudent.class);
        try{
            Response<Result<List<StudentFlowSubject>>> response3 = studentService
                    .GetStudentFlowByStudent(SemesterId,ID).execute();
            if(response3.isSuccessful())
                subjectsCount = response3.body().Data.size();
            else
                subjectsCount = 0;
        }
        catch(IOException e){
            subjectsCount = 0;
            Log.e("StudentInfoSimple",e.getMessage());
        }
    }
}
