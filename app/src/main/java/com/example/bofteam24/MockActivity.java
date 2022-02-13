package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MockActivity extends AppCompatActivity {

    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
        db = AppDatabase.singleton(this);

        Log.d("------------------------------------------------ IN MOCK ACTIVITY ", "...");
        if (StudentsListActivity.db != null) {
            if(StudentsListActivity.db.userDao() != null) {
                Log.d("------------ Everything below is Studentlist.db", "...");
                List<User> allUsers = StudentsListActivity.db.userDao().getAll();
                Log.d("------------ size of allUsers ", String.valueOf(allUsers.size()));
                Log.d("------------------------ user, userID ", "...");
                for(User user : allUsers) {
                    String userInfo = user.getName() + ", " + user.getUserId();
                    Log.d("------------------------ user ", userInfo);
                }
            }
            if(StudentsListActivity.db.courseDao() != null) {
                Log.d("------------ Everything below is Studentlist.db", "...");
                List<CourseRoom> allCourses = StudentsListActivity.db.courseDao().getAll();
                Log.d("------------ size of allCourses ", String.valueOf(allCourses.size()));
                Log.d("------------------------ course, userID, courseID", "...");
                for(CourseRoom course : allCourses) {
                    String courseInfo = course.getCourseName() + ", " + course.getUserId() + ", " + course.getCourseId();
                    Log.d("------------------------ course ", courseInfo);
                }
            }
        }

        AppDatabase db = AppDatabase.singleton(this);

        if(db.userDao() != null) {
            Log.d("------------ Everything below is Appdatabase db", "...");
            List<User> allUsers = db.userDao().getAll();
            Log.d("------------ size of allUsers ", String.valueOf(allUsers.size()));
            Log.d("------------------------ user, userID ", "...");
            for(User user : allUsers) {
                String userInfo = user.getName() + ", " + user.getUserId();
                Log.d("------------------------ user ", userInfo);
            }
        }

        if(db.courseDao() != null) {
            Log.d("------------ Everything below is Appdatabase db", "...");
            List<CourseRoom> allCourses = db.courseDao().getAll();
            Log.d("------------ size of allCourses ", String.valueOf(allCourses.size()));
            Log.d("------------------------ user, userID ", "...");
            for(CourseRoom course : allCourses) {
                String courseInfo = course.getCourseName() + ", " + course.getUserId() + ", " + course.getCourseId();
                Log.d("------------------------ course ", courseInfo);
            }
        }
        Log.d("------------------------------------------------ LOGS DONE FOR MOCK ACTIVITY ", ".");
    }

    public void onEnterClick(View view) {
        EditText editText = this.findViewById(R.id.edit_text);
        String csvInfo = editText.getText().toString();
        StudentsListActivity.cameFromMock = true;
        if (!csvInfo.equals("")) {
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            SharedPreferences.Editor editor = sp.edit();
//
//            editor.putString("csv_info", csvInfo);
//            editor.apply();
            Message mMessage = new Message(csvInfo.getBytes());
            Nearby.getMessagesClient(this).subscribe(StudentsListActivity.messageListener);
            StudentsListActivity.messageListener.onFound(mMessage);
        }

        editText.setText("");
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // ------- comment everything from here to retain prev users ------
        if (StudentsListActivity.db != null) {
            StudentsListActivity.db.courseDao().deleteAll();
            StudentsListActivity.db.userDao().deleteAll();
        }
        if(this.db != null) {
            this.db.courseDao().deleteAll();
            this.db.userDao().deleteAll();
        }
        // ---------------------- to here ----------------------
        super.onDestroy();
    }
}