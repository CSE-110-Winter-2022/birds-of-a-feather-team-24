package com.example.bofteam24;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bofteam24.db.CourseRoom;

import java.util.List;
import java.util.function.Consumer;

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {
    private final List<CourseRoom> courses;
    private final Consumer<CourseRoom> onCourseRemoved;

    public CoursesViewAdapter(List<CourseRoom> courses, Consumer<CourseRoom> onCourseRemoved) {
        super();
        this.courses = courses;
        this.onCourseRemoved= onCourseRemoved;
    }

    public void addCourse(CourseRoom course) {
        this.courses.add(course);
        this.notifyItemInserted(this.courses.size() - 1);
    }

    public void removeCourse(int position) {
        this.courses.remove(position);
        this.notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public CoursesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_description_row, parent, false);

        return new ViewHolder(view, this::removeCourse, onCourseRemoved);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewAdapter.ViewHolder holder, int position) {
        holder.setCourse(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseTextView;
        private CourseRoom course;

        ViewHolder(View itemView, Consumer<Integer> removeCourse, Consumer<CourseRoom> onCourseRemoved) {
            super(itemView);
            this.courseTextView = itemView.findViewById(R.id.course_description_tv);

//            Button removeButton = itemView.findViewById(R.id.remove_note_button);
//            removeButton.setOnClickListener((view) -> {
//                removeNote.accept(this.getAdapterPosition());
//                onNoteRemoved.accept(note);
//            });
        }

        public void setCourse(CourseRoom course) {
            this.course = course;
            this.courseTextView.setText(course.courseName);
        }

    }
}
