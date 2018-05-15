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

public class HomePageFragment extends Fragment implements OnNeedUpdateDataListener {

    ViewPager pager;
    PagerAdapter pagerAdapter;

    private StudentPageFragment studentPageFragment;
    private TeacherPageFragment teacherPageFragment;


    public HomePageFragment() {
        // Required empty public constructor
    }


    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentPageFragment = StudentPageFragment.newInstance();
        teacherPageFragment = TeacherPageFragment.newInstance("123","123");
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
        pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        return v;
    }


    @Override
    public void onNeedUpdate() {
        if(studentPageFragment instanceof OnNeedUpdateDataListener)
            ((OnNeedUpdateDataListener) studentPageFragment).onNeedUpdate();
        if(teacherPageFragment instanceof OnNeedUpdateDataListener)
            ((OnNeedUpdateDataListener) teacherPageFragment).onNeedUpdate();
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        //TODO: нужно вынести в ресурсы как массив
        private String[] titles = {"Я студент", "Я преподаватель"};

        private Fragment[] childFragments = {studentPageFragment, teacherPageFragment};

        MyFragmentPagerAdapter(FragmentManager fm) {
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
            return childFragments[position].toString();
        }
    }
}
