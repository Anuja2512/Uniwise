package com.example.uniwise;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder>{

    private Context context;
    private String[] data;
    private String[] course;
    private String dept;

    public SubjectsAdapter(String[] data,String[] course,String dept,Context context)
    {
        this.data=data;
        this.course=course;
        this.dept=dept;
        this.context=context;
    }
    @NonNull
    @Override
    public SubjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_item_layout,parent,false);
        return new SubjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsViewHolder holder, int position) {
        final String title=data[position];
        String Ccode=course[position];
        holder.txtCode.setText(Ccode);
        holder.txtTitle.setText(title);
        holder.txtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subjectIntent = new Intent(context, ForumActivity.class);
                context.startActivity(subjectIntent);
            }
        });
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subjectIntent = new Intent(context, ForumActivity.class);
                subjectIntent.putExtra("SelectedSubject",title);
                subjectIntent.putExtra("dept",dept);
                context.startActivity(subjectIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }


    public class SubjectsViewHolder extends RecyclerView.ViewHolder{
        TextView txtCode;
        TextView txtTitle;

        public SubjectsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCode=itemView.findViewById(R.id.courseCode);
            txtTitle=itemView.findViewById(R.id.javaSub);
        }
    }
}
