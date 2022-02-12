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
import com.example.bofteam24.db.User;
import com.example.bofteam24.db.UserWithCourses;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
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
    AppDatabase db;
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
                i = i + 2;
            }
            else if (i >= 6) {
                String courseNumber = csvInfoDivided[i+3].substring(0,3);
                String courseYear = csvInfoDivided[i].substring(3,7);
                courseInfo = courseYear + " " + csvInfoDivided[i + 1] + " " + csvInfoDivided[i + 2]
                        + " " + courseNumber;
                i = i + 2;
            }
            allCoursesInfo.add(courseInfo);
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
                Random rand = new Random();
                int randNum = rand.nextInt(100);
                User newUser = new User(String.valueOf(randNum), firstName, photoURL);
                Log.d("------------test user entered, new user created name ", firstName);
                db.userDao().insert(newUser);
                StudentsListActivity.users = db.userDao().getAll();
                Log.d("------------test user entered, user list size ", String.valueOf(StudentsListActivity.users.size()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost message: " + new String(message.getContent()));
            }
        };

        db = AppDatabase.singleton(this);
        StudentsListActivity.users = db.userDao().getAll(); // ----------REMOVED
        Log.d("------------user list size ", String.valueOf(db.userDao().getAll().size()));
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

//        if (!csvInfo.equals(defaultString)) {
//            // Log.d("------------Entered csv info", csvInfo);
//            csvInfo = csvInfo.trim(); // clean up csv input
//            // Log.d("------------Entered csv info after trim", csvInfo);
//            mMessage = new Message(csvInfo.getBytes());
//
//            String[] csvInfoDivided = cleanCVSInput(csvInfo); // split the csv input into small
//            // strings according to ","
//            String[] firstNameAndPhotoUrl = getFirstNameAndUrl(csvInfoDivided);
//
//            String firstName = firstNameAndPhotoUrl[0]; // get first name: Bill
//            String photoURL = firstNameAndPhotoUrl[1]; // get the specific URL for profile pic
//            ArrayList<String> allCoursesInfo = getAllCoursesInfo(csvInfoDivided); // get Bill's courses
//            // Now we have firstName = Bill, photoURL = https://blah blah, allCoursesInfo = ["FA 2022 CSE 110", "SP 2021 CSE 11", etc]
//
//            // printAllCourses(allCoursesInfo);
//
//            // AppDatabase db = AppDatabase.singleton(getApplicationContext());
//            // db = AppDatabase.singleton(this);
//            Random rand = new Random();
//            int randNum = rand.nextInt(100);
//            User newUser = new User(String.valueOf(randNum), firstName, photoURL);
//            Log.d("------------test user entered, new user created name ", firstName);
//            db.userDao().insert(newUser);
//            users = db.userDao().getAll();
//            Log.d("------------test user entered, user list size ", String.valueOf(users.size()));
//
//        }

        Log.d("------------ RecyclerView stuff coming up ", "...");
        RecyclerView studentView = (RecyclerView) findViewById(R.id.student_view);
        Log.d("------------ StudentViewAdapter stuff coming up ", "...");
        Log.d("------------ StudentsListActivity.users.size() ", String.valueOf(StudentsListActivity.users.size()));
        StudentViewAdapter adapter = new StudentViewAdapter(StudentsListActivity.users);
        Log.d("------------ studentView.setAdapter(adapter) ", "...");
        studentView.setAdapter(adapter);
        Log.d("------------ studentView.setLayoutManager(new LinearLayoutManager(this)) ", "...");
        studentView.setLayoutManager(new LinearLayoutManager(this));
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);

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
        if (!this.csvInfo.equals(defaultString)) {
            Nearby.getMessagesClient(this).unsubscribe(messageListener);
            this.messageListener.onLost(this.mMessage);
        }

        Nearby.getMessagesClient(this).unpublish(new Message("I am the user".getBytes()));
    }
}
