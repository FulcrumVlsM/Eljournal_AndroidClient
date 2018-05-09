package com.fulcrumvm.eljclient.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.fulcrumvm.eljclient.R;
import com.fulcrumvm.eljclient.apiinteract.APIUser;
import com.fulcrumvm.eljclient.model.Account;
import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.User;
import com.toptoche.searchablespinnerlibrary.SearchableListDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignUpFragment extends Fragment implements View.OnClickListener{

    //задача фрагмента: вывести окно для регистрации (обработкой отправки данных будет
    //                   заниматься LoginActivity

    SearchableSpinner userSpinner;
    EditText loginEditText;
    EditText passwordEditText;
    EditText passwordRepeatEditText;
    EditText secretWordEditText;
    ActionProcessButton submitButton;

    private OnSignUpFragmentInteractionListener mListener;
    public ArrayAdapter<User> userAdapter;
    public List<User> users = new ArrayList<>(50);

    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_up, container, false);
        userSpinner = (SearchableSpinner) v.findViewById(R.id.person_input_spinner);
        getUsers(null);
        userAdapter = new ArrayAdapter<User>(getActivity(),android.R.layout.simple_spinner_item,users);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userAdapter.setNotifyOnChange(true);
        userSpinner.setAdapter(userAdapter);

        loginEditText = v.findViewById(R.id.login_create_input);
        passwordEditText = v.findViewById(R.id.password_create_input);
        passwordEditText = v.findViewById(R.id.password_repeat_input);
        passwordRepeatEditText = v.findViewById(R.id.secret_word_input);
        secretWordEditText = v.findViewById(R.id.secret_word_input);
        submitButton = v.findViewById(R.id.sign_up_submit_button);

        submitButton.setOnClickListener(this);

        //задаю кнопке стиль бесконечной загрузки
        submitButton.setMode(ActionProcessButton.Mode.ENDLESS);
        submitButton.setProgress(0);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpFragmentInteractionListener) {
            mListener = (OnSignUpFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_up_submit_button){
            submitButton.setProgress(50);

            Account newAccount = new Account();
            newAccount.Secret = secretWordEditText.getText().toString();
            newAccount.Login = loginEditText.getText().toString();
            newAccount.Password = passwordEditText.getText().toString();
            User selectedUser = (User)userSpinner.getSelectedItem();
            newAccount.PersonID = selectedUser.ID;

            String repPassword = passwordRepeatEditText.getText().toString();
            if(newAccount.Password.compareTo(repPassword) == 0){
                boolean result = mListener.OnSubmit(newAccount);
                AsyncTask<String,String,String> asyncTask = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        try{
                            Thread.sleep(2000);
                        }
                        catch(InterruptedException e){}
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        submitButton.setProgress(-1);
                    }
                };
                asyncTask.execute("1");
            }
        }
    }


    public interface OnSignUpFragmentInteractionListener {
        boolean OnSubmit(Account newAccount);
    }


    private void getUsers(String template){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://eljournal.ddns.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIUser userService = retrofit.create(APIUser.class);
        userService.GetUsers(template,null).enqueue(new Callback<Result<List<User>>>() {
            @Override
            public void onResponse(Call<Result<List<User>>> call, Response<Result<List<User>>> response) {
                if(response.isSuccessful()){
                    if(response.code()!=204){
                        users.clear();
                        users.addAll(response.body().Data);
                    }
                }
                else
                    users.clear();
            }

            @Override
            public void onFailure(Call<Result<List<User>>> call, Throwable t) {
                users.clear();
            }
        });
    }

}
