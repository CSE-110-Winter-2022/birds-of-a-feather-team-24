package com.example.bofteam24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddClassesActivity extends AppCompatActivity {

    private Spinner yearSpinner;
    private Spinner quarterSpinner;
    private EditText subjectField;
    private EditText courseNumField;
    private AppDatabase db;
    private CoursesViewAdapter coursesViewAdapter;

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
                "user id example", courseDesc);
        db.courseDao().insert(newCourse);
//        coursesViewAdapter.addCourse(newCourse);
        List<CourseRoom> courseRoomList = db.courseDao().getForUser(1);
        printCourseList(courseRoomList);
    }

    private boolean checkIfFieldsAreValid(String subject, String courseNum) {
        return subject.chars().allMatch(Character::isLetter)
                && courseNum.chars().allMatch(Character::isLetterOrDigit) && subject.length() > 0
                && courseNum.length() > 0;
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

        boolean fieldsValid = checkIfFieldsAreValid(subject, courseNumber);

        if (!fieldsValid) {
            //create prompt
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Inputs must be non-empty and include zero spaces." +
                    " Subject can only include letters." +
                    " Course # can include letters and numbers");
            builder.setTitle("Alert!");
            builder.setCancelable(true);

            builder
                    .setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }

                    );
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            //Using Room Database
            String courseDesc = String.format("%s %s %s %d", subject.toUpperCase(),
                    courseNumber.toUpperCase(), quarter, year);
            addNewCourseToDatabase(courseDesc);

            Toast.makeText(this, "Course added!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCancelClick(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}