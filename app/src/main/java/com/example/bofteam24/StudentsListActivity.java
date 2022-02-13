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

public class StudentsListActivity extends AppCompatActivity {

    private static final String TAG = "BoF-Team24";
    public static MessageListener messageListener;
    public static AppDatabase db;
    public static List<User> users;
    public static List<CourseRoom> allCoursesInfo;
    public static boolean cameFromMock = false;

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
                csvInfoDivided[i] = csvInfoDivided[i].replace("\n", "-"); //--> added the "-"
            }
//            Log.d("---------------- index", index);
//            Log.d("--- csvInfoDivided[i]", csvInfoDivided[i]);
//            Log.d("--- size of ^", String.valueOf(csvInfoDivided[i].length()));
        }

        return csvInfoDivided;
    }

    /*
    Prints all the courses of a student in this format: 2021 FA CSE 110.
    Used only for debugging.
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
                String courseNumber = csvInfoDivided[i+3].split("-")[0];
                String courseYear = csvInfoDivided[i];
                courseInfo = courseYear + " " + csvInfoDivided[i + 1] + " " + csvInfoDivided[i + 2]
                        + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 2;
            }
            else if (i >= 6) {
                String courseNumber = csvInfoDivided[i+3].split("-")[0];
                String courseYear = csvInfoDivided[i].split("-")[1];

                courseInfo = courseYear + " " + csvInfoDivided[i + 1] + " " + csvInfoDivided[i + 2]
                        + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 2;
            }
        }

        return allCoursesInfo;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        messageListener = new MessageListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFound(@NonNull Message message) {
                String messageString = new String(message.getContent());
                Log.d(TAG, "Found message: " + messageString);
                // Log.d("------------messageString ", messageString);
                messageString = messageString.trim();
                String[] csvInfoDivided = cleanCVSInput(messageString);
                String[] firstNameAndPhotoUrl = getFirstNameAndUrl(csvInfoDivided);
                String firstName = firstNameAndPhotoUrl[0];
                String photoURL = firstNameAndPhotoUrl[1];
                ArrayList<String> allCoursesString = getAllCoursesInfo(csvInfoDivided);
                // allCoursesInfoString = ["2022 WI CSE 110", "2021 WI CSE 11", "2022 SP MMW 121", etc]

                // this needs to be actual courses
                String[] myCourses = {"2022 WI CSE 110", "2021 FA CSE 140", "2021 FA CSE 101"}; //random

                int sameCourses = 0;
                for(String myCourse : myCourses) {
                    for(String otherCourse : allCoursesString) {
                        if (myCourse.equals(otherCourse)) {
                            sameCourses+=1;
                        }
                    }
                }

                if (sameCourses == 0) { return; }

                Random rand = new Random();
                int userID = rand.nextInt(1000);
                String userIDString = String.valueOf(userID);
                Log.d("------------------ user ID when creating user ", userIDString);
                User newUser = new User(userIDString, firstName, photoURL, sameCourses);
                StudentsListActivity.db.userDao().insert(newUser);

                for (String oneCourse : allCoursesString) {
                    int courseID = rand.nextInt(10000);
                    CourseRoom course = new CourseRoom(courseID, userIDString, oneCourse);
                    StudentsListActivity.db.courseDao().insert(course);
                }
                StudentsListActivity.allCoursesInfo = StudentsListActivity.db.courseDao().getAll();
                StudentsListActivity.users = StudentsListActivity.db.userDao().getAll();
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost message: " + new String(message.getContent()));
            }
        };

        StudentsListActivity.db = AppDatabase.singleton(this);
        StudentsListActivity.users = StudentsListActivity.db.userDao().getAll();
        StudentsListActivity.allCoursesInfo = StudentsListActivity.db.courseDao().getAll();

        Nearby.getMessagesClient(this).subscribe(StudentsListActivity.messageListener);

        Nearby.getMessagesClient(this).publish(new Message("I am the user".getBytes()));

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // ------- comment everything from here  ----------------------
        if(StudentsListActivity.db != null) {
            StudentsListActivity.db.courseDao().deleteAll();
            StudentsListActivity.db.userDao().deleteAll();
        }
        // ---------------------- to here to retain prev users ----------------------
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if (StudentsListActivity.cameFromMock) {
        Message lostMessage = new Message("lost signal".getBytes());
        Nearby.getMessagesClient(this).unsubscribe(StudentsListActivity.messageListener);
        StudentsListActivity.messageListener.onLost(lostMessage);
        //}

        Nearby.getMessagesClient(this).unpublish(new Message("I am the user".getBytes()));
    }

    public void onGoToProfileClicked(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
