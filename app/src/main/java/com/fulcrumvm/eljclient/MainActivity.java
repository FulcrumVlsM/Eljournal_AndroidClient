package com.fulcrumvm.eljclient;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fulcrumvm.eljclient.apiinteract.APIFaculty;
import com.fulcrumvm.eljclient.fragments.HomePageFragment;
import com.fulcrumvm.eljclient.fragments.OnLoadDataListener;
import com.fulcrumvm.eljclient.fragments.OnNeedUpdateDataListener;
import com.fulcrumvm.eljclient.fragments.StudentPageFragment;
import com.fulcrumvm.eljclient.model.Faculty;
import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnLoadDataListener,
        StudentPageFragment.OnStudentPageFragmentInteractionListener{

    private Retrofit retrofit;
    private User user;
    private String login;
    private String password;
    private String token;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    Fragment homeFragment;
    FragmentTransaction fTrans;

    private final int LOGIN_REQUEST_CODE = 1335;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(getLogin() == null || getPassword() == null || getTokenInStorage() == null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent,LOGIN_REQUEST_CODE);
        }
        else{
            login = getLogin();
            password = getPassword();
            token = getTokenInStorage();
        }

        homeFragment = HomePageFragment.newInstance();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.root_content_layout, homeFragment);
        fTrans.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onClick(View v){
        retrofit = new Retrofit.Builder().baseUrl("http://eljournal.ddns.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIFaculty apiService = retrofit.create(APIFaculty.class);
        apiService.GetFaculty("66E25DCB-1EFF-4B5F-86BA-CD686C503EE0").enqueue(new Callback<Result<Faculty>>() {
            @Override
            public void onResponse(Call<Result<Faculty>> call, Response<Result<Faculty>> response) {

            }

            @Override
            public void onFailure(Call<Result<Faculty>> call, Throwable t) {
                Log.e("failure",t.toString());
            }
        });

    };


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_student) {

        } else if (id == R.id.nav_home) {
            Log.d("nav_student", "Выбрана 'МояСтраница'");
            homeFragment = HomePageFragment.newInstance();
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.add(R.id.root_content_layout, homeFragment);
            fTrans.commit();
        } else if (id == R.id.nav_message) {

        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout){
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "Сработал OnActivityResult");
        if(requestCode == LOGIN_REQUEST_CODE){
            if(resultCode != RESULT_OK || data == null){
                finish();
                return;
            }

            login = data.getStringExtra("login");
            password = data.getStringExtra("password");
            token = data.getStringExtra("token");

            if(login == null && password == null && token == null){
                finish();
                return;
            }

            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString("login", login);
            editor.putString("password", password);
            editor.putString("token", token);
            editor.commit();

            notifyDataUpdated();
        }
    }


    //данный метод должен вызывать индикатор загрузки поверх экрана приложения
    @Override
    public void onLoadStateChanged(boolean state) {
        //TODO: индикатор еще не реализован
        if (state)
            Toast.makeText(this,"Идет загрузка",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Загрузка завершена",Toast.LENGTH_SHORT).show();
    }

    //при ошибке загрузки данных. Хз, что делать. По ходу кинуть пользователю ошибку
    @Override
    public void onFailure() {
        //TODO: метод еще не реализован
    }

    @Override
    public String getToken() {
        return token;
    }

    //реализация методов интерфейсов от фрагментов
    @Override
    public void OpenSubjectList(String studentId) {
        //TODO: вывод фрагмента с данными по успеваемости студента
    }


    private String getLogin(){
        return getPreferences(MODE_PRIVATE).getString("login", null);
    }
    private String getPassword(){
        return getPreferences(MODE_PRIVATE).getString("password", null);
    }
    private String getTokenInStorage(){
        return getPreferences(MODE_PRIVATE).getString("token", null);
    }

    private void notifyDataUpdated(){
        ((OnNeedUpdateDataListener) homeFragment).onNeedUpdate();
        //здесь будут и другие фрагменты, которые вызывает вызывает данное активити
    }

    private void logOut(){
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        recreate();
    }
}
