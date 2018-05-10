package com.fulcrumvm.eljclient.apiinteract;

import com.fulcrumvm.eljclient.model.Account;
import com.fulcrumvm.eljclient.model.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIAccount {
    @POST("/api/Account")
    Call<Void> AddAccount(@Body Account newAccount);

    @POST("/api/Account/auth")
    Call<Result<String>> Auth(@Body Account account);
}
