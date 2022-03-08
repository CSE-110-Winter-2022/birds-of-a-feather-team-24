package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FavStudentsListActivity extends AppCompatActivity {

    private AppDatabase db;
    private List<User> favUsers;
    private List<CourseRoom> allCoursesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_students_list);

        db = AppDatabase.singleton(this);
        favUsers = db.userDao().getFavUsers(true);
        allCoursesInfo = db.courseDao().getAll();

        Collections.sort(favUsers, Comparator.comparing(User::getNumOfSameCourses));
        Collections.reverse(favUsers);

        RecyclerView favStudentView = findViewById(R.id.fav_student_view);
        LinearLayoutManager favStudentLayoutManager = new LinearLayoutManager(this);
        favStudentView.setLayoutManager(favStudentLayoutManager);
        StudentViewAdapter favStudentViewAdapter = new StudentViewAdapter(favUsers);
        favStudentView.setAdapter(favStudentViewAdapter);
    }

    public void onFavBackClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}