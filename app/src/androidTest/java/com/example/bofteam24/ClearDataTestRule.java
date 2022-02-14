package com.example.bofteam24;

import android.app.Activity;
import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import com.example.bofteam24.db.AppDatabase;

import org.junit.Rule;


public class ClearDataTestRule<T extends Activity> extends ActivityTestRule<T> {

    public ClearDataTestRule(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched(){
        super.beforeActivityLaunched();
        AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
        db.userDao().deleteAll();
        db.courseDao().deleteAll();
        Context context = InstrumentationRegistry.getTargetContext();
        context.getSharedPreferences("USER_SHARED_PREF", Context.MODE_PRIVATE)
        .edit()
        .clear()
        .commit();
    }
}
