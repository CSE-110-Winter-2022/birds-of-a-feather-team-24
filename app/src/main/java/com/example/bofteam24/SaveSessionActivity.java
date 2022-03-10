package com.example.bofteam24;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bofteam24.db.AppDatabase;

public class SaveSessionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_session);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
