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

public class FavStudentViewAdapter extends RecyclerView.Adapter<FavStudentViewAdapter.ViewHolder> {
    private List<User> students;

    public FavStudentViewAdapter(List<User> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public FavStudentViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View studentView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.student_row, parent, false);

        return new FavStudentViewAdapter.ViewHolder(studentView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavStudentViewAdapter.ViewHolder holder, int position) {
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

        //
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch favSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            studentButton = (Button) itemView.findViewById(R.id.student_button);
            profilePic = (ImageButton) itemView.findViewById(R.id.profile_pic);
            numCommonCourses = (TextView) itemView.findViewById(R.id.num_common_courses);
            //
            favSwitch = itemView.findViewById(R.id.switch_fav);
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

            this.favSwitch.setChecked(false);
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
