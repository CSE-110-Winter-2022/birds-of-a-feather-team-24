package com.example.bofteam24;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.Session;

import java.util.ArrayList;
import java.util.List;

public class SaveSessionActivity extends AppCompatActivity {

    private AppDatabase db;
    private Session session;
    private Spinner courseMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_session);
        setTitle("Name Session");

        int sessionId = (int)getIntent().getLongExtra("sessionId", -1);
        if(sessionId == -1) throw new RuntimeException("Session ID not passed to SaveSessionActivity.class");

        db = AppDatabase.singleton(this);
        session = db.sessionDao().getSessionById(sessionId);

        courseMenu = findViewById(R.id.current_courses_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        String myUserId = UserSelf.getInstance(this).getUserId();
        List<CourseRoom> currentCourses = db.courseDao().getCurrentCourses(myUserId, "2022 WI"); // 2022 WI
//        List<CourseRoom> allOfMyCourses = db.courseDao().getForUser(myUserId);
//        List<CourseRoom> myCoursesSpecifics = new ArrayList<>();
//        for(CourseRoom cr : allOfMyCourses) {
//            String courseName = cr.getCourseName();
//            String yearAndQuarter = courseName.split(" ")[0] + courseName.split(" ")[1];
//        }
        Log.d(ParseUtils.TAG, "-------------------- IN SaveSessionActivity");
        Log.d(ParseUtils.TAG, "--------------------- in SaveSessionActivity currentCourses: " + currentCourses.toString());
        for (CourseRoom cr : currentCourses) {
            Log.d(ParseUtils.TAG, "--------------------- in SaveSessionActivity currentCourses: " + cr.getCourseName());
        }

        // TODO: remove hardcoded value after merge with sorting branch
        for(CourseRoom course : currentCourses) {
            String courseName = course.getCourseName(); // 2022 WI CSE 110
            String subjectAndNumber = courseName.split(" ")[2] + courseName.split(" ")[3];
            Log.d(ParseUtils.TAG, "--------------------- in SaveSessionActivity subjectAndNum: " + subjectAndNumber);
//            int firstSpace = courseName.indexOf(' ');
//            int secondSpace = courseName.indexOf(' ', firstSpace + 1);
            adapter.add(subjectAndNumber);
        }
        courseMenu.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void saveSessionName(String name) {
        session.setSessionName(name);
        db.sessionDao().insert(session);
        Toast.makeText(this, "Saved session as " + name, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onSaveCustomClick(View view) {
        TextView sessionNameField = findViewById(R.id.session_name_text_field);

        if (!sessionNameField.getText().toString().equals("")) {
            saveSessionName(sessionNameField.getText().toString());
        }
        else {
            saveSessionName(session.getSessionName());
        }
    }

    public void onSaveCurrentClick(View view) {
        saveSessionName(courseMenu.getSelectedItem().toString());
    }

    public void onCancelSaveSessionClick(View view) {
        Intent intent = new Intent(this, StudentsListActivity.class);
        startActivity(intent);
    }
}