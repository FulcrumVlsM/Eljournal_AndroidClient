package com.fulcrumvm.eljclient.model;

import android.support.annotation.NonNull;

import com.fulcrumvm.eljclient.apiinteract.APIUser;

import java.io.Serializable;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User implements Serializable {
    public String ID;
    public String Surname;
    public String Name;
    public String Patronymic;
    public String Info;
    public String RoleId;

    public List<Department> Departments;
    public List<Faculty> Faculties;

    @Override
    public String toString() {
        return String.format("%s %s %s", Surname, Name, Patronymic);
    }


    public static void GetMe(Callback<Result<User>> callback, @NonNull String token, @NonNull String apiPath){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(apiPath)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIUser userService = retrofit.create(APIUser.class);
        userService.GetMe(token).enqueue(callback);
    }
}
