package com.example.bofteam24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.example.bofteam24.db.UserWithCourses;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class StudentsListActivity extends AppCompatActivity {

    public static AppDatabase db;
    public static List<User> users;
    public static List<CourseRoom> allCoursesInfo;
    public static boolean cameFromMock = false;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        db = AppDatabase.singleton(this);
        users = db.userDao().getOthers(UserSelf.getInstance(this).getUserId());
        allCoursesInfo = db.courseDao().getAll();

        //StudentsListActivity.users.sort(Comparator.comparing(User::getNumOfSameCourses));
        Collections.sort(StudentsListActivity.users, Comparator.comparing(User::getNumOfSameCourses));
        Collections.reverse(StudentsListActivity.users);

        // before
//        RecyclerView studentView = (RecyclerView) findViewById(R.id.student_view);
//        StudentViewAdapter adapter = new StudentViewAdapter(StudentsListActivity.users);
//        studentView.setAdapter(adapter);
//        studentView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView studentView = findViewById(R.id.student_view);
        LinearLayoutManager studentLayoutManager = new LinearLayoutManager(this);
        studentView.setLayoutManager(studentLayoutManager);
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(StudentsListActivity.users);
        studentView.setAdapter(studentViewAdapter);
    }

    public void onStopClick(View view) {
        if (MockActivity.messageListener != null) {
            Message lostMessage = new Message("lost signal".getBytes());
            Nearby.getMessagesClient(this).unsubscribe(MockActivity.messageListener);
            MockActivity.messageListener.onLost(lostMessage);
            Nearby.getMessagesClient(this).unpublish(new Message("I am the user".getBytes()));
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // ------- comment everything from here  ----------------------
//        if(StudentsListActivity.db != null) {
//            StudentsListActivity.db.courseDao().deleteAll();
//            StudentsListActivity.db.userDao().deleteAll();
//        }
        // ---------------------- to here to retain prev users ----------------------
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if (StudentsListActivity.cameFromMock) {
//        Message lostMessage = new Message("lost signal".getBytes());
//        Nearby.getMessagesClient(this).unsubscribe(StudentsListActivity.messageListener);
//        StudentsListActivity.messageListener.onLost(lostMessage);
//        //}
//
//        Nearby.getMessagesClient(this).unpublish(new Message("I am the user".getBytes()));
    }
}
