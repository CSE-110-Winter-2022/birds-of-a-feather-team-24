package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;

import java.util.Arrays;
import java.util.List;

public class AddClassesActivity extends AppCompatActivity {

    private Spinner yearSpinner;
    private Spinner quarterSpinner;
    private EditText subjectField;
    private EditText courseNumField;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

        db = AppDatabase.singleton(this);
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        quarterSpinner = (Spinner) findViewById(R.id.quarter_spinner);
        subjectField = (EditText) findViewById(R.id.subject_text_input);
        courseNumField = (EditText) findViewById(R.id.course_num_input);

        loadFields();

        Spinner yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

        Spinner quarterSpinner = (Spinner) findViewById(R.id.quarter_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.quarters, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_textview_align);
        quarterSpinner.setAdapter(adapter2);

    }

    private void loadFields() {
        // Course[] courses = Course.getAll(getApplicationContext());
        // clarify if needs to persist after closing
    }

    private void printCourseList(List<CourseRoom> courseRoomList) {
        for (CourseRoom course : courseRoomList) {
            System.out.println(course.toString());
        }
    }

    private void addNewCourseToDatabase(String courseDesc) {
        CourseRoom newCourse = new CourseRoom(db.courseDao().maxId()+1,
                1, courseDesc);
        db.courseDao().insert(newCourse);
        List<CourseRoom> courseRoomList = db.courseDao().getForUser(1);
        printCourseList(courseRoomList);
    }

    public void onEnterClick(View view) {
        int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
        String quarter = quarterSpinner.getSelectedItem().toString();
        String subject = subjectField.getText().toString();
        String courseNumber = courseNumField.getText().toString();
//
//        Course course = new Course(year, quarter, subject, courseNumber);
//        course.create(getApplicationContext());

//        System.out.println(Arrays.toString(Course.getAll(getApplicationContext())));


        //Using Room Database
        String courseDesc = String.format("%s %s %s %d", subject, courseNumber,
                quarter, year);
        addNewCourseToDatabase(courseDesc);
    }

    public void onCancelClick(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
//        finish();
    }
}