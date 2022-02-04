package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

public class AddClassesActivity extends AppCompatActivity {

    private Spinner yearSpinner;
    private Spinner quarterSpinner;
    private EditText subjectField;
    private EditText courseNumField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

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
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(adapter2);

    }

    private void loadFields() {
        // Course[] courses = Course.getAll(getApplicationContext());
        // clarify if needs to persist after closing
    }

    public void onEnterClick(View view) {
        int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
        String quarter = quarterSpinner.getSelectedItem().toString();
        String subject = subjectField.getText().toString();
        String courseNumber = courseNumField.getText().toString();

        Course course = new Course(year, quarter, subject, courseNumber);
        course.create(getApplicationContext());

        System.out.println(Arrays.toString(Course.getAll(getApplicationContext())));
    }

    public void onCancelClick(View view) {
        finish();
    }

}