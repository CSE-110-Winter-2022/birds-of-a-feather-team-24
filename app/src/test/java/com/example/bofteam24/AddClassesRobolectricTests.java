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

import org.junit.runner.manipulation.Ordering;
import org.robolectric.annotation.Config;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertEquals;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.MockAppDatabase;

@RunWith(AndroidJUnit4.class)
public class AddClassesRobolectricTests {
    public MockAppDatabase testDb;

    @Rule
    public ActivityScenarioRule<AddClassesActivity> scenarioRule = new ActivityScenarioRule<>(AddClassesActivity.class);

    @Before
    public void setUp() throws Exception{
        Context context = ApplicationProvider.getApplicationContext();
        testDb = MockAppDatabase.singleton(context);
//        testDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }
    @After
    public void after() throws Exception {
        testDb.closeDatabase();
    }

    private void setClassFields(Spinner yearSpinner, Spinner quarterSpinner, EditText subjectField,
                                EditText courseNumField, int yearSel, int quarterSel,
                                String subject, String courseNum) {
        yearSpinner.setSelection(yearSel);
        quarterSpinner.setSelection(quarterSel);
        subjectField.setText(subject);
        courseNumField.setText(courseNum);
    }

    @Test
    public void testPersistsData() {
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
    public void testEnterClassInDatabase() {
        ActivityScenario<AddClassesActivity> scenario = scenarioRule.getScenario();
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

            System.out.println("testEnterClassInDatabase()");

            Button enterButton = (Button) activity.findViewById(R.id.enter_class_button);
            enterButton.performClick();

            int courseId = testDb.courseDao().maxId();
            String courseDesc = testDb.courseDao().get(courseId).toString();

            assertEquals(courseDesc, "CSE 110 Fall 2019");
        });
    }

    @Test
    public void testEnterAdditionalClassesToDatabase() {
        System.out.println("testEnterAdditionalClassesToDatabase()");
        ActivityScenario<AddClassesActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            Spinner yearSpinner = (Spinner) activity.findViewById(R.id.year_spinner);
            Spinner quarterSpinner = (Spinner) activity.findViewById(R.id.quarter_spinner);
            EditText subjectField = (EditText) activity.findViewById(R.id.subject_text_input);
            EditText courseNumField = (EditText) activity.findViewById(R.id.course_num_input);
            Button enterButton = (Button) activity.findViewById(R.id.enter_class_button);

            setClassFields(yearSpinner, quarterSpinner, subjectField, courseNumField, 1,
                    1, "CSE", "140");

            enterButton.performClick();

            setClassFields(yearSpinner, quarterSpinner, subjectField, courseNumField, 1,
                    1, "CSE", "140L");
            enterButton.performClick();

            setClassFields(yearSpinner, quarterSpinner, subjectField, courseNumField, 1,
                    1, "CAT", "125R");
            enterButton.performClick();

            assertEquals(testDb.courseDao().count(), 3);
        });
    }

}
