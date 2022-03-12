package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.User;

import java.util.List;

public class LoadedSessionActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_session);
        db = AppDatabase.singleton(this);

        Integer sessionId = getIntent().getIntExtra("sessionId", -1);
        Log.d("------------Opening sessionId: " , sessionId.toString());
        Log.d("-----------All sessionId in database: ", db.sessionDao().getAllSessionIds().toString());
        List<User> usersFromSession = db.sessionDao().getUsersBySessionId(sessionId);
        Log.d("----------Users in session: ", usersFromSession.toString());

        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SortUsers.byStrategy(LoadedSessionActivity.this, sortSpinner, usersFromSession);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                SortUsers.byStrategy(LoadedSessionActivity.this, sortSpinner, usersFromSession);
            }
        });

        SortUsers.byStrategy(LoadedSessionActivity.this, sortSpinner, usersFromSession);

        RecyclerView studentView = findViewById(R.id.session_student_view);
        LinearLayoutManager studentLayoutManager = new LinearLayoutManager(this);
        studentView.setLayoutManager(studentLayoutManager);
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(usersFromSession);
        studentView.setAdapter(studentViewAdapter);
    }

    public void onBackClicked(View view) {
        finish();
    }
}