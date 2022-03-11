package com.example.bofteam24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

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
    private Message myMessage;
    private String myMessageString;
    private MessageListener messageListener;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        db = AppDatabase.singleton(this);

        // everything below is mocking
        if (MockActivity.incomingMessagesString == null || MockActivity.incomingMessagesString.size() == 0) {
            Log.d("------------- incomingMessagesString IS NULL", "...");
        }
        if (MockActivity.incomingMessagesString != null && MockActivity.incomingMessagesString.size() > 0) {
            Log.d("------------- incomingMessagesString is NOT NULL", "...");
            for (String messageString : MockActivity.incomingMessagesString) {
                // Log.d("----- the string that came from mock activity ", messageString);
                messageListener.onFound(new Message(messageString.getBytes()));
            }
        }

        users = db.userDao().getOthers(UserSelf.getInstance(this).getUserId());
        allCoursesInfo = db.courseDao().getAll();

        Collections.sort(StudentsListActivity.users, Comparator.comparing(User::getNumOfSameCourses));
        Collections.reverse(StudentsListActivity.users);

        RecyclerView studentView = findViewById(R.id.student_view);
        LinearLayoutManager studentLayoutManager = new LinearLayoutManager(this);
        studentView.setLayoutManager(studentLayoutManager);
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(StudentsListActivity.users);
        studentView.setAdapter(studentViewAdapter);

//        messageListener = new MessageListener() {
//            @Override
//            public void onFound(@NonNull Message message) {
//                String s = "Found message: " + new String(message.getContent());
//                Log.d("Found message", s);
//                Toast.makeText(StudentsListActivity.this, s, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLost(@NonNull Message message) {
//                String s = "Lost message: " + new String(message.getContent());
//                Log.d("Lost message", s);
//            }
//        };
        messageListener = new MockMessageListener(StudentsListActivity.this);

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
        myMessage = new Message(myMessageString.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Nearby.getMessagesClient(this).publish(myMessage);
        Log.d(ParseUtils.TAG, "-------- Published my message" + "\n" + myMessageString);
        // sending your message done

        Nearby.getMessagesClient(this).subscribe(messageListener);
        Log.d("-------- Subscribed to Message Listener", "...");
    }

    public void onStopClick(View view) {
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
        if (messageListener != null) {

            Nearby.getMessagesClient(this).unpublish(myMessage);
            Log.d(ParseUtils.TAG, "-------- Unpublished my message" + "\n" + myMessageString);

            Nearby.getMessagesClient(this).unsubscribe(messageListener);
            Log.d(ParseUtils.TAG, "-------- Unsubscribed Message Listener");

            // this is for mocking
            if (MockActivity.incomingMessagesString != null && MockActivity.incomingMessagesString.size() > 0) {
                for (String messageString : MockActivity.incomingMessagesString) {
                    messageListener.onLost(new Message(messageString.getBytes()));
                }
            }
        }
    }

}
