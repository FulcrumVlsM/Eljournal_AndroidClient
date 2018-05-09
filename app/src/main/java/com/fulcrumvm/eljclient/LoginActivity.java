package com.fulcrumvm.eljclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

import com.fulcrumvm.eljclient.fragments.LoginFragment;
import com.fulcrumvm.eljclient.fragments.SignInFragment;
import com.fulcrumvm.eljclient.fragments.SignUpFragment;
import com.fulcrumvm.eljclient.model.Account;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements SignUpFragment.OnSignUpFragmentInteractionListener,
        LoginFragment.OnLoginFragmentInteractionListener{

    private static final int REQUEST_READ_CONTACTS = 0;

    private LoginFragment loginFragment;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;

    private FragmentTransaction fTransact;


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

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }



    //Событие, когда пользователь отправляет данные для регистрации
    @Override
    public boolean OnSubmit(Account newAccount) {
        return false;
    }

    //обработка перехода на страницу авторизации
    @Override
    public void onSignIn() {
        fTransact = getSupportFragmentManager().beginTransaction();
        if(fTransact != null){
            fTransact.replace(R.id.login_main,signInFragment);
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


    private class RegistrationAsyncTask extends AsyncTask<Account,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Account... accounts) {
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }


    }
}

