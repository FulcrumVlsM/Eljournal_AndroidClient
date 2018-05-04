package com.fulcrumvm.eljclient;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fulcrumvm.eljclient.apiinteract.APIService;
import com.fulcrumvm.eljclient.model.Faculty;
import com.fulcrumvm.eljclient.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button mybutton;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mybutton = (Button) findViewById(R.id.myButton);
        mybutton.setOnClickListener(listener);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                345);
        }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            retrofit = new Retrofit.Builder().baseUrl("http://eljournal.ddns.net")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIService apiService = retrofit.create(APIService.class);
            apiService.getData("66E25DCB-1EFF-4B5F-86BA-CD686C503EE0").enqueue(new Callback<Result<Faculty>>() {
                @Override
                public void onResponse(Call<Result<Faculty>> call, Response<Result<Faculty>> response) {
                    if(response.isSuccessful())
                        mybutton.setText(response.body().Data.Name);
                    else {
                        mybutton.setText("Ошибка");
                        Log.d("http_response",String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Result<Faculty>> call, Throwable t) {
                    mybutton.setText("Ошибка очень плохая");
                    Log.e("failure",t.toString());
                }
            });
        }
    };
}
