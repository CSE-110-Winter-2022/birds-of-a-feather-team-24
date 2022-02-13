package com.example.bofteam24;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bofteam24.db.User;
import com.example.bofteam24.db.UserWithCourses;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {
    private List<User> students;

    public StudentViewAdapter(List<User> students) {
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
        User student = students.get(position);
        holder.setStudent(student);
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Button studentButton;
        private final ImageButton profilePic;
        private final TextView numCommonCourses;
        private User student;


        public ViewHolder(View itemView) {
            super(itemView);
            studentButton = (Button) itemView.findViewById(R.id.student_button);
            profilePic = (ImageButton) itemView.findViewById(R.id.profile_pic);
            numCommonCourses = (TextView) itemView.findViewById(R.id.num_common_courses);
        }

        public void setStudent(User student) {
            this.student = student;

            Picasso.get().load(student.getPhotoUrl()).into(this.profilePic);
            this.profilePic.setEnabled(true);

            String studentFirstName = this.student.getName().split(" ")[0];
            this.studentButton.setText(studentFirstName);
            this.studentButton.setEnabled(true);

            int commonCourses = this.student.getNumOfSameCourses();
            this.numCommonCourses.setText(String.valueOf(commonCourses));
            this.numCommonCourses.setEnabled(true);
        }

        @Override
        public void onClick(View view) {
//            Context context = view.getContext();
//            Intent intent = new Intent(context, MainActivity.class);
//            context.startActivity(intent);
            Log.d("--------Student button or student image clicked", ".....");
        }
    }
}
