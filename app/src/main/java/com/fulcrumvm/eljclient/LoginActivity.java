package com.fulcrumvm.eljclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.fulcrumvm.eljclient.apiinteract.APIAccount;
import com.fulcrumvm.eljclient.fragments.LoginFragment;
import com.fulcrumvm.eljclient.fragments.SignInFragment;
import com.fulcrumvm.eljclient.fragments.SignUpFragment;
import com.fulcrumvm.eljclient.model.Account;
import com.fulcrumvm.eljclient.model.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements SignUpFragment.OnSignUpFragmentInteractionListener,
        LoginFragment.OnLoginFragmentInteractionListener,
        SignInFragment.OnSignInFragmentInteractionListener{

    private static final int REQUEST_READ_CONTACTS = 0;

    private LoginFragment loginFragment;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;

    private FragmentTransaction fTransact;

    private String _login;
    private String _password;
    private String _token;

    private void goToMain(){
        Intent intent = new Intent();
        intent.putExtra("login",_login);
        intent.putExtra("password", _password);
        intent.putExtra("token", _token);
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginFragment = LoginFragment.newInstance();
        signInFragment = SignInFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();

        fTransact = getSupportFragmentManager().beginTransaction();
        fTransact.add(R.id.login_main, loginFragment);
        //fTransact.addToBackStack(null);
        fTransact.commit();
    }



    //обработка перехода на страницу авторизации
    @Override
    public void onSignIn() {
        fTransact = getSupportFragmentManager().beginTransaction();
        if(fTransact != null){
            fTransact.replace(R.id.login_main, signInFragment);
            fTransact.addToBackStack(null);
            fTransact.commit();
        }
        else
            Log.e("LoginActivity", "Empty FragmentTransaction");
    }

    //обработка перехода на страницу регистрации
    @Override
    public void onSignUp() {
        fTransact = getSupportFragmentManager().beginTransaction();
        if(fTransact != null){
            fTransact.replace(R.id.login_main, signUpFragment);
            fTransact.addToBackStack(null);
            fTransact.commit();
        }
        else
            Log.e("LoginActivity", "Empty FragmentTransaction");
    }


    //вызывается фрагментом, выполняет регистрацию нового аккаунта
    @Override
    public void SignUp(Account newAccount, final ActionProcessButton submitButton) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://eljournal.ddns.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIAccount accountService = retrofit.create(APIAccount.class);
        accountService.AddAccount(newAccount).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    submitButton.setProgress(100);
                    //TODO: автоматически логиним пользователя по созданному аккаунту, отдаем результат и закрываем активность
                }
                else{
                    submitButton.setProgress(-1);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                submitButton.setProgress(-1);
                Toast.makeText(getApplicationContext(),"Нет связи с сервером",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //вызывается фрагментом, выполняет авторизацию
    @Override
    public void SignIn(final Account account, final ActionProcessButton submitButton) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://eljournal.ddns.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIAccount accountService = retrofit.create(APIAccount.class);
        accountService.Auth(account).enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                if(response.isSuccessful()){
                    _token = response.body().Data;
                    _login = account.Email;
                    _password = account.Password;
                    submitButton.setProgress(100);

                    Intent intent = new Intent();
                    intent.putExtra("login", _login);
                    intent.putExtra("password", _password);
                    intent.putExtra("token", _token);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    _token = null;
                    _login = null;
                    _password = null;
                    submitButton.setProgress(-1);
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                _token = null;
                _login = null;
                _password = null;
                submitButton.setProgress(-1);
                Log.e("SignIn error", t.getMessage());
            }
        });
    }



}

