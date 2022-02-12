package com.example.bofteam24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StudentsListActivity extends AppCompatActivity {

    private static final String TAG = "BoF-Team24";
    final String defaultString = "default String";
    MessageListener messageListener;
    Message mMessage;
    String csvInfo;
    public static AppDatabase db;
    public static List<User> users; // ----------ADDED
    public static boolean cameFromMock = false;
    //ArrayList<User> users = new ArrayList<User>();

    /*
    Method to clean up the incoming cvs input. Remove spaces, new lines, etc.
    This way it is easier to obtain the data that is needed.
     */
    private String[] cleanCVSInput(String csvInfo) {

        String[] csvInfoDivided = csvInfo.split(",");
        String arrSize = String.valueOf(csvInfoDivided.length);

//        Log.d("---------------- size of  csvInfoDivided", arrSize);
        for(int i = 0; i < csvInfoDivided.length; i++) {
            String index = String.valueOf(i);
            if (i >=3 ) {
                csvInfoDivided[i] = csvInfoDivided[i].trim();
                csvInfoDivided[i] = csvInfoDivided[i].replace("\n", ""); // added
            }
//            Log.d("---------------- index", index);
//            Log.d("--- csvInfoDivided[i]", csvInfoDivided[i]);
//            Log.d("--- size of ^", String.valueOf(csvInfoDivided[i].length()));
        }

        return csvInfoDivided;
    }

    /*
    Prints all the courses of a student in this format: 2021 FA CSE 110
     */
    private void printAllCourses(ArrayList<String> allCoursesInfo) {
        for(String cInfo : allCoursesInfo) {
            Log.d("------------Course Info", cInfo);
        }
    }

    /*
    Method to get the first name and the url from the cvs input.
     */
    private String[] getFirstNameAndUrl(String[] csvInfoDivided) {
        String firstName = "";
        String photoURL = "";
        for(int i = 0; i < csvInfoDivided.length; i++) {
            if (i == csvInfoDivided.length - 1) {
                break;
            }
            if (i == 0) {
                firstName = csvInfoDivided[i];
            } else if (i == 3) {
                photoURL = csvInfoDivided[i];
            }
        }

        return new String[]{firstName, photoURL};
    }

    /*
    Method to get all the courses of a user from the cvs input.
     */
    private ArrayList<String> getAllCoursesInfo(String[] csvInfoDivided) {
        ArrayList<String> allCoursesInfo = new ArrayList<String>();
        String courseInfo = "";

        for(int i = 0; i < csvInfoDivided.length; i++) {
            if (i == csvInfoDivided.length-1) {break;}
            else if (i == 6) {
                String courseNumber = csvInfoDivided[i+3].substring(0,3);
                String courseYear = csvInfoDivided[i];
                courseInfo = courseYear + " " + csvInfoDivided[i + 1] + " " + csvInfoDivided[i + 2]
                        + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 2;
            }
            else if (i >= 6) {
                String courseNumber = csvInfoDivided[i+3].substring(0,3);
                String courseYear = csvInfoDivided[i].substring(3,7);
                courseInfo = courseYear + " " + csvInfoDivided[i + 1] + " " + csvInfoDivided[i + 2]
                        + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 2;
            }
            //allCoursesInfo.add(courseInfo);
        }

        return allCoursesInfo;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        messageListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
                String messageString = new String(message.getContent());
                Log.d("------------messageString ", messageString);
                messageString = messageString.trim();
                String[] csvInfoDivided = cleanCVSInput(messageString);
                String[] firstNameAndPhotoUrl = getFirstNameAndUrl(csvInfoDivided);
                String firstName = firstNameAndPhotoUrl[0];
                String photoURL = firstNameAndPhotoUrl[1];
                ArrayList<String> allCoursesInfo = getAllCoursesInfo(csvInfoDivided);
                // allCoursesInfo = ["2022 WI CSE 110", "2021 WI CSE 11", "2022 SP MMW 121", etc]
                Random rand = new Random();
                int userID = rand.nextInt(1000);
                String userIDString = String.valueOf(userID);
                User newUser = new User(userIDString, firstName, photoURL);
                StudentsListActivity.db.userDao().insert(newUser);
                Log.d("------------test user entered, new user created name ", firstName);
                for (String oneCourseInfo : allCoursesInfo) {
                    int courseID = rand.nextInt(10000);
                    String courseInfoAndID = oneCourseInfo + ", " + String.valueOf(courseID);
                    Log.d("------------------------info for one course ", courseInfoAndID);
                    // oneCourseInfo is a string such as: "2022 WI CSE 110"
                    CourseRoom course = new CourseRoom(courseID, userIDString, oneCourseInfo);
                    StudentsListActivity.db.courseDao().insert(course);
                }
                StudentsListActivity.users = StudentsListActivity.db.userDao().getAll();
                Log.d("------------test user entered, user list size ", String.valueOf(StudentsListActivity.users.size()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                if (message == null) {
                    return;
                }
                Log.d(TAG, "Lost message: " + new String(message.getContent()));
            }
        };

        StudentsListActivity.db = AppDatabase.singleton(this);
        StudentsListActivity.users = StudentsListActivity.db.userDao().getAll(); // ----------REMOVED
        Log.d("------------user list size ", String.valueOf(StudentsListActivity.db.userDao().getAll().size()));
        //mMessage = new Message("Hello World".getBytes());

        // get the same shared preference from MockActivity.java
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.csvInfo = sp.getString("csv_info", defaultString);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
        Log.d("------------ is cvsInfo \"default string\" or an actual test? ", csvInfo);
        Log.d("------------ came from mock? ", String.valueOf(StudentsListActivity.cameFromMock));

        if(StudentsListActivity.cameFromMock) {
            if (!csvInfo.equals(defaultString)) {
                this.mMessage = new Message(csvInfo.getBytes());

                Nearby.getMessagesClient(this).subscribe(messageListener);
                this.messageListener.onFound(this.mMessage);
                //            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
            }
        }
        else {
            Log.d("------------ cameFromMock = false ", "...");
            Nearby.getMessagesClient(this).subscribe(messageListener);
        }
        Nearby.getMessagesClient(this).publish(new Message("I am the user".getBytes()));

        Log.d("------------ RecyclerView stuff coming up ", "...");
        RecyclerView studentView = (RecyclerView) findViewById(R.id.student_view);
        Log.d("------------ StudentViewAdapter stuff coming up ", "...");
        StudentViewAdapter adapter = new StudentViewAdapter(StudentsListActivity.users);
        Log.d("------------ studentView.setAdapter(adapter) ", "...");
        studentView.setAdapter(adapter);
        Log.d("------------ studentView.setLayoutManager(new LinearLayoutManager(this)) ", "...");
        studentView.setLayoutManager(new LinearLayoutManager(this));

        List<User> allUsers = StudentsListActivity.db.userDao().getAll();
        Log.d("------------ size of allUsers ", String.valueOf(StudentsListActivity.users.size()));
        for(User user : allUsers) {
            String userInfo = user.getName() + ", " + user.getUserId();
            Log.d("------------------------ user ", userInfo);
        }

        List<CourseRoom> allCourses = StudentsListActivity.db.courseDao().getAll();
        Log.d("------------ size of all courses ", String.valueOf(allCourses.size()));
        for(CourseRoom course : allCourses) {
            String courseInfo = course.getCourseName() + ", " + course.getUserId() + ", " + course.getCourseId();
            Log.d("------------------------ course ", courseInfo);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onStopClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!this.csvInfo.equals(defaultString)) {
//            Nearby.getMessagesClient(this).subscribe(messageListener);
//            this.messageListener.onFound(this.mMessage);
//        }
//
//        Nearby.getMessagesClient(this).publish(new Message("I am the user".getBytes()));
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (StudentsListActivity.cameFromMock) {
            if (!this.csvInfo.equals(defaultString)) {
                Nearby.getMessagesClient(this).unsubscribe(messageListener);
                this.messageListener.onLost(this.mMessage);
            }
        }

        Nearby.getMessagesClient(this).unpublish(new Message("I am the user".getBytes()));
    }
}
