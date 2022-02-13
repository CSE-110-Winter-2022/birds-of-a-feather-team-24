package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    final String firstRun = "firstrun";


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
    }
}