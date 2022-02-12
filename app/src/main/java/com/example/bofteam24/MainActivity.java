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
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.Nearby;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase db = AppDatabase.singleton(this);
        db.userDao().deleteAll();
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