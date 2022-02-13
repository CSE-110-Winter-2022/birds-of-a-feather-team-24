package com.example.bofteam24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.Nearby;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("USER_SHARED_PREF", MODE_PRIVATE);
        if(pref.getString("ID", "").equals("")) {
            Intent loginAct = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginAct);
        }
        else {
            Intent listAct = new Intent(MainActivity.this, StudentsListActivity.class);
            startActivity(listAct);
        }

        db = AppDatabase.singleton(this);
    }

    @Override
    protected void onDestroy() {
        // ------- comment everything from here to retain prev users ------
        if (StudentsListActivity.db != null) {
            StudentsListActivity.db.courseDao().deleteAll();
            StudentsListActivity.db.userDao().deleteAll();
        }
        if(this.db != null) {
            this.db.courseDao().deleteAll();
            this.db.userDao().deleteAll();
        }
        // ---------------------- to here ----------------------
        super.onDestroy();
    }

    public void onStartClick(View view) {
        Intent intent = new Intent(this, StudentsListActivity.class);
        startActivity(intent);
    }

    public void onMockClick(View view) {
        Intent intent = new Intent(this, MockActivity.class);
        startActivity(intent);
    }
}
