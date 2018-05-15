package com.fulcrumvm.eljclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fulcrumvm.eljclient.R;

public class StudentSubjectListPage extends Fragment {
    private static final String STUDENT = "studentId";

    private String studentId;



    public StudentSubjectListPage() {
        // Required empty public constructor
    }


    public static StudentSubjectListPage newInstance(String studentId) {
        StudentSubjectListPage fragment = new StudentSubjectListPage();
        Bundle args = new Bundle();
        args.putString(STUDENT, studentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentId = getArguments().getString(STUDENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
