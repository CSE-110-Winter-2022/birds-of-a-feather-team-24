package com.example.bofteam24;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        ArrayList<Course> dummyCourses1 = new ArrayList<Course>();
        dummyCourses1.add(new Course(2020, "fall", "cse", "11"));
        dummyCourses1.add(new Course(2020, "winter", "cse", "12"));

        ArrayList<Course> dummyCourses2 = new ArrayList<Course>();
        dummyCourses2.add(new Course(2020, "fall", "cse", "20"));
        dummyCourses2.add(new Course(2020, "winter", "cse", "21"));

        ArrayList<DummyStudent> dummyStudents = new ArrayList<DummyStudent>();
        dummyStudents.add(new DummyStudent("Abe", "Lincoln",dummyCourses1));
        dummyStudents.add(new DummyStudent("John", "Doe", dummyCourses2));
        dummyStudents.add(new DummyStudent("FirstName", "LastName", null));

        RecyclerView studentView = (RecyclerView) findViewById(R.id.student_view);
        // studentView.setAlpha(0); // recycle view disappears

        StudentViewAdapter adapter = new StudentViewAdapter(dummyStudents);
        studentView.setAdapter(adapter);
        studentView.setLayoutManager(new LinearLayoutManager(this));

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
}
