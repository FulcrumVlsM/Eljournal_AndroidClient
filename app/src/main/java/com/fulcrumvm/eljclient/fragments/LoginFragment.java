package com.fulcrumvm.eljclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fulcrumvm.eljclient.R;


public class LoginFragment extends Fragment
        implements View.OnClickListener {
    //фрагмент выводит главное окно входа с кнопками для входа или регистрации

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnLoginFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.authorization_main,container,false);
        Button signIn = (Button) v.findViewById(R.id.sign_in_button);
        Button signUp = (Button) v.findViewById(R.id.sign_up_button);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener)
            mListener = (OnLoginFragmentInteractionListener) context;
        else
            throw new RuntimeException("This activity is not implemented" +
                    " OnloginFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //обработчик нажатия кнопок "вход" и "регистрация"
    @Override
    public void onClick(View v) {
        if(mListener != null){
            switch(v.getId()){
                case R.id.sign_in_button:
                    mListener.onSignIn();
                    break;
                case R.id.sign_up_button:
                    mListener.onSignUp();
            }
        }
        else
            Log.e("LoginActivity","Empty OnLoginFragmentInteractionListener");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginFragmentInteractionListener {
        void onSignIn();
        void onSignUp();
    }
}
