package com.example.bofteam24;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//import org.junit.runner.manipulation.Ordering;
//import org.robolectric.annotation.Config;
//import org.robolectric.Robolectric;

import static org.junit.Assert.assertEquals;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseDao;
import com.example.bofteam24.db.MockAppDatabase;
import com.example.bofteam24.db.UserDao;
import com.google.ar.core.Config;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class AddClassesRobolectricTests {
    private UserDao userDao;
    private CourseDao courseDao;
    private AppDatabase db;

    @Rule
    public ActivityScenarioRule<AddClassesActivity> scenarioRule = new ActivityScenarioRule<>(AddClassesActivity.class);

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        db = AppDatabase.singleton(context);
        userDao = db.userDao();
        courseDao = db.courseDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testPersistsFields() {
        ActivityScenario<AddClassesActivity> scenario = ActivityScenario.launch(AddClassesActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            Spinner yearSpinner = (Spinner) activity.findViewById(R.id.year_spinner);
            Spinner quarterSpinner = (Spinner) activity.findViewById(R.id.quarter_spinner);
            EditText subjectField = (EditText) activity.findViewById(R.id.subject_text_input);
            EditText courseNumField = (EditText) activity.findViewById(R.id.course_num_input);

            yearSpinner.setSelection(0);
            quarterSpinner.setSelection(0);
            subjectField.setText("CSE");
            courseNumField.setText("110");

            Button enterButton = (Button) activity.findViewById(R.id.enter_class_button);
            enterButton.performClick();

            assertEquals(0, yearSpinner.getSelectedItemPosition());
            assertEquals(0, quarterSpinner.getSelectedItemPosition());
            assertEquals("CSE", subjectField.getText().toString());
            assertEquals("110", courseNumField.getText().toString());
        });
    }

    @Test
    public void testCancelAddingCourse() {
        ActivityScenario<AddClassesActivity> scenario = ActivityScenario.launch(AddClassesActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        int numCoursesInitial = db.courseDao().count();
        int numCoursesAfter;

        scenario.onActivity(activity -> {
            Spinner yearSpinner = (Spinner) activity.findViewById(R.id.year_spinner);
            Spinner quarterSpinner = (Spinner) activity.findViewById(R.id.quarter_spinner);
            EditText subjectField = (EditText) activity.findViewById(R.id.subject_text_input);
            EditText courseNumField = (EditText) activity.findViewById(R.id.course_num_input);

            yearSpinner.setSelection(0);
            quarterSpinner.setSelection(0);
            subjectField.setText("CSE");
            courseNumField.setText("110");



            Button cancelButton = (Button) activity.findViewById(R.id.cancel_class_button);
            cancelButton.performClick();

            assertEquals(0, yearSpinner.getSelectedItemPosition());
            assertEquals(0, quarterSpinner.getSelectedItemPosition());
            assertEquals("CSE", subjectField.getText().toString());
            assertEquals("110", courseNumField.getText().toString());
        });

        numCoursesAfter = courseDao.count();

        assertEquals(numCoursesInitial, numCoursesAfter);
    }

    @Test
    public void tryToAddInvalidSubjectTest() {
        ActivityScenario<AddClassesActivity> scenario = ActivityScenario.launch(AddClassesActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        int numCoursesInitial = db.courseDao().count();
        int numCoursesAfter;

        scenario.onActivity(activity -> {
            Spinner yearSpinner = (Spinner) activity.findViewById(R.id.year_spinner);
            Spinner quarterSpinner = (Spinner) activity.findViewById(R.id.quarter_spinner);
            EditText subjectField = (EditText) activity.findViewById(R.id.subject_text_input);
            EditText courseNumField = (EditText) activity.findViewById(R.id.course_num_input);

            yearSpinner.setSelection(0);
            quarterSpinner.setSelection(0);
            subjectField.setText("CSE110");
            courseNumField.setText("110");

            Button enterButton = (Button) activity.findViewById(R.id.enter_class_button);
            enterButton.performClick();

        });
        numCoursesAfter = courseDao.count();
        assertEquals(numCoursesInitial, numCoursesAfter);
    }

    @Test
    public void tryToAddInvalidCourseNumberTest() {
        ActivityScenario<AddClassesActivity> scenario = ActivityScenario.launch(AddClassesActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        int numCoursesInitial = db.courseDao().count();
        int numCoursesAfter;

        scenario.onActivity(activity -> {
            Spinner yearSpinner = (Spinner) activity.findViewById(R.id.year_spinner);
            Spinner quarterSpinner = (Spinner) activity.findViewById(R.id.quarter_spinner);
            EditText subjectField = (EditText) activity.findViewById(R.id.subject_text_input);
            EditText courseNumField = (EditText) activity.findViewById(R.id.course_num_input);

            yearSpinner.setSelection(0);
            quarterSpinner.setSelection(0);
            subjectField.setText("CSE");
            courseNumField.setText("110 123");

            Button enterButton = (Button) activity.findViewById(R.id.enter_class_button);
            enterButton.performClick();

        });
        numCoursesAfter = courseDao.count();
        assertEquals(numCoursesInitial, numCoursesAfter);
    }
}
