package com.fulcrumvm.eljclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.fulcrumvm.eljclient.R;
import com.fulcrumvm.eljclient.model.Account;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private OnSignInFragmentInteractionListener mListener;

    private EditText loginEditText;
    private EditText passwordEditText;
    private ActionProcessButton submitButton;


    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sign_in, container,false);
        loginEditText = v.findViewById(R.id.login_input);
        passwordEditText = v.findViewById(R.id.password_input);
        submitButton = v.findViewById(R.id.sign_in_submit_button);

        submitButton.setOnClickListener(this);
        submitButton.setMode(ActionProcessButton.Mode.ENDLESS);
        submitButton.setCompleteText("Готово");
        submitButton.setErrorText("Неверный email или пароль");

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignInFragmentInteractionListener) {
            mListener = (OnSignInFragmentInteractionListener) context;
        } else {
            mListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        submitButton.setProgress(50);
        Account account = new Account();
        account.Email = loginEditText.getText().toString();
        account.Password = passwordEditText.getText().toString();
        if(mListener != null)
            mListener.SignIn(account, submitButton);
    }

    //Этот интерфейс описывает как активность будет реагировать на действия из фрагмента
    public interface OnSignInFragmentInteractionListener {
        void SignIn(Account account, ActionProcessButton submitButton);
    }
}
