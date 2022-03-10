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
    public static Message myMessage;
    public static String myMessageString;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        db = AppDatabase.singleton(this);
        users = db.userDao().getOthers(UserSelf.getInstance(this).getUserId());
        allCoursesInfo = db.courseDao().getAll();

        Collections.sort(StudentsListActivity.users, Comparator.comparing(User::getNumOfSameCourses));
        Collections.reverse(StudentsListActivity.users);

        // everything below is for sending your own message to other devices
        String userId = UserSelf.getInstance(this).getUserId();
        User user = db.userDao().getUserWithId(userId);
        List<CourseRoom> courses = db.courseDao().getForUser(userId);
        String userName = user.getName();
        String photoURL = user.getPhotoUrl();
        List<String> stringCourses = new ArrayList<>();

        for(int i = 0; i < courses.size(); i++) {
            CourseRoom course = courses.get(i);
            String stringCourse = course.toMockString();
            stringCourse = stringCourse.replaceAll(" ", ",");
            if (i != courses.size()-1) { stringCourse += "\n"; }
            stringCourses.add(stringCourse);
        }

        StringBuilder myMessageBuilder = new StringBuilder(userName + ",,,\n" + photoURL + ",,,\n");

        for(String course : stringCourses) {
            myMessageBuilder.append(course);//.append("\n");
        }

        myMessageString = myMessageBuilder.toString();
        myMessage = new Message(myMessageString.getBytes());

        Nearby.getMessagesClient(this).publish(myMessage);
        Log.i("Published my message", myMessageString);
        // sending your message done


        RecyclerView studentView = findViewById(R.id.student_view);
        LinearLayoutManager studentLayoutManager = new LinearLayoutManager(this);
        studentView.setLayoutManager(studentLayoutManager);
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(StudentsListActivity.users);
        studentView.setAdapter(studentViewAdapter);
    }

    public void onStopClick(View view) {
        if (MockActivity.messageListener != null) {
            Nearby.getMessagesClient(this).unsubscribe(MockActivity.messageListener);
            Log.i("Unsubscribe Message Listener", "...");
            MockActivity.messageListener.onLost(MockActivity.mMessage);
            Nearby.getMessagesClient(this).unpublish(myMessage);
            Log.i("Unpublished my message", myMessageString);
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
