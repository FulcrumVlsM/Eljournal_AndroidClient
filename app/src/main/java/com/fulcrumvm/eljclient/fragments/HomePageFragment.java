package com.fulcrumvm.eljclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fulcrumvm.eljclient.R;
import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.Student;
import com.fulcrumvm.eljclient.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageFragment extends Fragment {

    ViewPager pager;
    PagerAdapter pagerAdapter;
    private String token;
    private User user;
    private String apiPath;

    @NonNull
    FragmentManager fragmentManager;

    private OnHomePageFragmentInteractionListener mListener;

    public HomePageFragment() {
        // Required empty public constructor
    }


    public static HomePageFragment newInstance(@NonNull String token) {
        HomePageFragment fragment = new HomePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiPath = getContext().getResources().getString(R.string.api_url);
        token = getActivity().getPreferences(Context.MODE_PRIVATE).getString("token", null);
        if(token != null)
            User.GetMe(getUserCallback, token, apiPath);
        else
            user = new User();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_bar_main, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = ((AppCompatActivity) getActivity()).findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Действие не назначено", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pager = v.findViewById(R.id.pager);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnHomePageFragmentInteractionListener){
            mListener = (OnHomePageFragmentInteractionListener) context;
        }
        else
            mListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnHomePageFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[] titles = {"Я студент", "Я преподаватель"};

        private Fragment[] childFragments = {StudentPageFragment.newInstance(user, token),
                                             TeacherPageFragment.newInstance("123","143")};

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return childFragments[position];
        }

        @Override
        public int getCount() {
            return childFragments.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    Callback<Result<User>> getUserCallback = new Callback<Result<User>>() {
        @Override
        public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
            if(response.isSuccessful())
                user = response.body().Data;
            else
                user = new User();

            pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
            pager.setAdapter(pagerAdapter);
        }

        @Override
        public void onFailure(Call<Result<User>> call, Throwable t) {
            user = new User();
            Log.e("getUserCallback", t.getMessage());
        }
    };
}
