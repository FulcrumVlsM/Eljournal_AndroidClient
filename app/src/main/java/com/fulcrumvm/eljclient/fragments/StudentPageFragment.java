package com.fulcrumvm.eljclient.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fulcrumvm.eljclient.R;
import com.fulcrumvm.eljclient.adapters.StudentGroupDataAdapter;
import com.fulcrumvm.eljclient.apiinteract.APIStudent;
import com.fulcrumvm.eljclient.model.Result;
import com.fulcrumvm.eljclient.model.Student;
import com.fulcrumvm.eljclient.model.User;
import com.fulcrumvm.eljclient.model.advanced.StudentInfoSimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentPageFragment extends Fragment implements View.OnClickListener, StudentGroupDataAdapter.OnItemClickListener{
    private static final String PARAM1 = "user";
    private static final String PARAM2 = "token";

    private static String apiPath;

    private User user;
    private String token;
    private List<StudentInfoSimple> studentList;

    RecyclerView studentRecyclerView;
    StudentGroupDataAdapter adapter;

    private OnStudentPageFragmentInteractionListener mListener;

    public StudentPageFragment() {

    }


    public static StudentPageFragment newInstance(User user, String token) {
        StudentPageFragment fragment = new StudentPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM1,user);
        args.putString(PARAM2, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiPath = getActivity().getApplicationContext().getResources().getString(R.string.api_url);
        studentList = new ArrayList<>();
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(PARAM1);
            token = getArguments().getString(PARAM2);

            //в отдельном потоке наполняю список по данным обучения
            StudentListMakerTask studentListMakerTask = new StudentListMakerTask();
            studentListMakerTask.execute(user.ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student, container, false);

        studentRecyclerView = v.findViewById(R.id.student_groups_recView);
        adapter = new StudentGroupDataAdapter(getActivity(), (Fragment)this,studentList);
        studentRecyclerView.setAdapter(adapter);
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClickListener(View v) {
        int itemPosition = studentRecyclerView.getChildLayoutPosition(v);
        String semName = studentList.get(itemPosition).semester.Name;
        Toast.makeText(getContext(),semName,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnStudentPageFragmentInteractionListener)
            mListener = (OnStudentPageFragmentInteractionListener) context;
        else
            mListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        studentList.clear();
    }

    public interface OnStudentPageFragmentInteractionListener {
        void OpenSubjectList(String semesterId, String studentId);
    }




    private class StudentListMakerTask extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            List<Student> students = new ArrayList<>();

            //получаю список студентов
            for(String userId : strings){
                Retrofit retrofit = new Retrofit.Builder().baseUrl(apiPath)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIStudent studentService = retrofit.create(APIStudent.class);
                try{
                    Response<Result<List<Student>>> response = studentService.GetByUser(userId).execute();
                    if(response.isSuccessful())
                        students.addAll(response.body().Data);
                }
                catch(IOException e){
                    Log.e("Error", e.getMessage());
                }
            }

            //из списка студентов получаю развернутый список студентов для вывода в список
            for(Student student : students){
                studentList.add(new StudentInfoSimple(student, apiPath));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }
}
