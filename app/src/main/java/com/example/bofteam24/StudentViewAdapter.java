package com.example.bofteam24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {
    private List<DummyStudent> students;

    public StudentViewAdapter(List<DummyStudent> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View studentView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.student_row, parent, false);

        return new StudentViewAdapter.ViewHolder(studentView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewAdapter.ViewHolder holder, int position) {
        DummyStudent student = students.get(position);
        holder.studentButton.setText(student.getFirstName());
        holder.studentButton.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Button studentButton;
        private DummyStudent student;

        public ViewHolder(View itemView) {
            super(itemView);
            studentButton = (Button) itemView.findViewById(R.id.student_button);
        }

        public void setStudent(DummyStudent student) {
            this.student = student;
            this.studentButton.setText(this.student.getFirstName());
        }

        @Override
        public void onClick(View view) {
            //System.out.println("Student button clicked");
        }
    }
}
