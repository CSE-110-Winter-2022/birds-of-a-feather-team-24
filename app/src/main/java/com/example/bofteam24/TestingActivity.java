package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        String userId = getIntent().getExtras().getString("user_id", null);
        Log.d("-------------------------- user ID in testing activity ", userId);
    }

    public void onTestBackClicked(View view) {
        Intent intent = new Intent(this, StudentsListActivity.class);
        startActivity(intent);
    }
}