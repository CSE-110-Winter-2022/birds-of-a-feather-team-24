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
import com.example.bofteam24.sorting.SortingStrategy;

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
        List<CourseRoom> allOfMyCourses = db.courseDao().getForUser(myUserId);
        String current = "2022WI";
        for(CourseRoom cr : allOfMyCourses) {
            String courseName = cr.getCourseName();
            String yearAndQuarter = courseName.split(" ")[0] + courseName.split(" ")[1];
            System.out.println(yearAndQuarter + "; " + current);
            if(yearAndQuarter.equals(current)) {
                String subjectAndNumber = courseName.split(" ")[2] + courseName.split(" ")[3];
                adapter.add(subjectAndNumber);
            }
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