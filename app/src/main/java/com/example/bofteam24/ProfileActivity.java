package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;
    private AppDatabase db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //update
        setTitle("Update to name of user");

        db = AppDatabase.singleton(this);
        List<CourseRoom> courses = db.courseDao().getAll();

        System.out.println("printing courses");
        for (CourseRoom course : courses) {
            System.out.println(course.toString());
        }

        //Set up the recycler view to show our database contents.
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        coursesViewAdapter = new CoursesViewAdapter(courses, (course) -> {
            db.courseDao().delete(course);
        });
        coursesRecyclerView.setAdapter(coursesViewAdapter);
    }
}