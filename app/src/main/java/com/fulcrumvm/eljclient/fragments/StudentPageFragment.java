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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentPageFragment extends Fragment implements
        View.OnClickListener,
        StudentGroupDataAdapter.OnItemClickListener,
        OnNeedUpdateDataListener{
    private static final String PARAM1 = "user";
    private static final String PARAM2 = "token";

    private static String apiPath;

    //данные, загружаемые с сервера
    private User user;
    private String token;
    private List<StudentInfoSimple> studentList;

    RecyclerView studentRecyclerView;
    StudentGroupDataAdapter adapter;

    private OnStudentPageFragmentInteractionListener mListener;
    private OnLoadDataListener loadListener;

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


    //метод для загрузки всех данных о студенте
    private void loadData(){
        token = mListener.GetToken();
        User.GetMe(new Callback<Result<User>>() {
            @Override
            public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                if(response.isSuccessful()){
                    user = response.body().Data;
                    new StudentListMakerTask().execute(user.ID);
                }
                else{
                    user = new User();
                    loadListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Result<User>> call, Throwable t) {
                user = new User();
                loadListener.onFailure();
            }
        }, token, apiPath);

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClickListener(View v) {
        //TODO: здесь сделать переход к инфе студента по семестру
        int itemPosition = studentRecyclerView.getChildLayoutPosition(v);
        String semName = studentList.get(itemPosition).semester.Name;
        Toast.makeText(getContext(),semName,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnStudentPageFragmentInteractionListener)
            mListener = (OnStudentPageFragmentInteractionListener) context;
        if(context instanceof OnLoadDataListener)
            loadListener = (OnLoadDataListener) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        studentList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //уведомление от активности с требованием обновить данные
    @Override
    public void onNeedUpdate() {
        //TODO: необходимо обновить данные
    }

    public interface OnStudentPageFragmentInteractionListener {
        void OpenSubjectList(String studentId);
        String GetToken();
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
            loadListener.onLoadStateChanged(false);
        }

        @Override
        protected void onPreExecute() {
            studentList.clear();
        }
    }
}
