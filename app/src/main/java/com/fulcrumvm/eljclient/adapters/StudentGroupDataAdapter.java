package com.fulcrumvm.eljclient.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fulcrumvm.eljclient.R;
import com.fulcrumvm.eljclient.model.advanced.StudentInfoSimple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StudentGroupDataAdapter extends RecyclerView.Adapter<StudentGroupDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<StudentInfoSimple> studentInfoSimpleList;

    private OnItemClickListener itemClickListener;

    public StudentGroupDataAdapter(Context context, Fragment fragment, List<StudentInfoSimple> studentInfoSimpleList){
        this.studentInfoSimpleList = studentInfoSimpleList;
        this.inflater = LayoutInflater.from(context);

        if(fragment instanceof OnItemClickListener)
            itemClickListener = (OnItemClickListener) fragment;
        else
            itemClickListener = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.group_semester, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClickListener(v);
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentInfoSimple student = studentInfoSimpleList.get(position);
        holder.semesterName.setText(student.semester.Name);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //2018-01-15T00:00:00
        try{
            Date startD = dateFormat.parse(student.semester.StartDate);
            Date endD = dateFormat.parse(student.semester.EndDate);

            SimpleDateFormat outFormat = new SimpleDateFormat("MM.yyyy");
            String startDStr = outFormat.format(startD);
            String endDStr = outFormat.format(endD);
            String dateStr = String.format("%s - %s", startDStr, endDStr);
            holder.semesterDate.setText(dateStr);
        }
        catch(ParseException e){
            Log.e("onBindViewHolder", e.getMessage());
        }

        holder.groupName.setText(student.group.Name);

        String subjCount = inflater.getContext().getResources().getString(R.string.subj_count);
        subjCount = String.format(subjCount,student.subjectsCount);
        holder.subjectCount.setText(subjCount);
    }

    @Override
    public int getItemCount() {
        return studentInfoSimpleList.size();
    }


    //класс ViewHolder, представляющий элемент списка RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView semesterName;
        final TextView groupName;
        final TextView semesterDate;
        final TextView subjectCount;

        public ViewHolder(View itemView) {
            super(itemView);

            this.semesterName = itemView.findViewById(R.id.group_semester_semName);
            this.groupName = itemView.findViewById(R.id.group_semester_groupName);
            this.semesterDate = itemView.findViewById(R.id.group_semester_semDate);
            this.subjectCount = itemView.findViewById(R.id.group_semester_subjCount);
        }
    }

    //интерфейс, через который обрабатывается нажатие на элемент списка
    public interface OnItemClickListener{
        void onItemClickListener(View v);
    }
}
