package com.example.bofteam24;
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
public class ShowListOfStudentsTests {

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

}
