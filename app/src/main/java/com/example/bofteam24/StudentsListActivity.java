package com.example.bofteam24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.Session;
import com.example.bofteam24.db.User;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class StudentsListActivity extends AppCompatActivity {

    public static AppDatabase db;
    public static List<User> users;
    public static List<CourseRoom> allCoursesInfo;
    String savedName;
    Session session;
    Long id;

    MessageListener messageListener;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        db = AppDatabase.singleton(this);
        users = db.userDao().getOthers(UserSelf.getInstance(this).getUserId());
        allCoursesInfo = db.courseDao().getAll();

        Date currentTime = Calendar.getInstance().getTime();
        String time = currentTime.toString();
        session = new Session(null, time);
        id = db.sessionDao().insert(session);

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

        Intent intent = new Intent(this, SaveSessionActivity.class);
        intent.putExtra("sessionId", id);
        startActivity(intent);

//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        final EditText edittext = new EditText(this);
//        alert.setMessage("Enter Custom Save Name");
//        alert.setTitle("Name Session");
//
//        alert.setView(edittext);
//
//        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                savedName = edittext.getText().toString();
//                for(int i = 0; i < users.size(); i++) {
//                    Session session = new Session(0, savedName, users.get(i).getUserId());
//                }
//                Intent intent = new Intent(alert.getContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        alert.setNegativeButton("Select Current Enrolled Class", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//                // what ever you want to do with No option.
//                for(int i = 0; i < users.size(); i++) {
//                    Session session = new Session(0, savedName, users.get(i).getUserId());
//                }
//
//                Intent intent = new Intent(alert.getContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        alert.show();

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
