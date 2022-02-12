package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    final String firstRun = "firstrun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    public void onStartClick(View view) {
        Intent intent = new Intent(this, StudentsListActivity.class);
        startActivity(intent);
    }
}