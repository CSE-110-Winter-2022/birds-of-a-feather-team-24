package com.example.bofteam24;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AddClassesRobolectricTests {
    @Rule
    public ActivityScenarioRule<AddClassesActivity> scenarioRule = new ActivityScenarioRule<>(AddClassesActivity.class);

    @Test
    public void testPersistsData() {
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

            Button enterButton = (Button) activity.findViewById(R.id.enter_class_button);
            enterButton.performClick();

            assertEquals(0, yearSpinner.getSelectedItemPosition());
            assertEquals(0, quarterSpinner.getSelectedItemPosition());
            assertEquals("CSE", subjectField.getText().toString());
            assertEquals("110", courseNumField.getText().toString());
        });
    }

}
