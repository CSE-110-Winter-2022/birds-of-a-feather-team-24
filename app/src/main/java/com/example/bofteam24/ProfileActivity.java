package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private boolean different_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get user_id extra from intent to decide which layout to display

        different_user = false;

        if (different_user) {
            //change "my courses" -> "<other_user>'s courses"
            TextView coursesLabel = findViewById(R.id.my_courses_tv);
            coursesLabel.setText("(user_name)'s courses");

            //hide add course button
            Button add_course_button = findViewById(R.id.add_course_button);
            add_course_button.setVisibility(View.GONE);
        }

        //TODO:
        //list other user's courses
        //set profile pic to user's
        //setTitle to name of user

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

    public void onAddCourseClicked(View view) {
        Intent intent = new Intent(this, AddClassesActivity.class);
        startActivity(intent);
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}