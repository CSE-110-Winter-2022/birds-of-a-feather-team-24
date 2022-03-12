package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
        Log.i("------------Opening sessionId: " , sessionId.toString());
        Log.i("-----------All sessionId in database: ", db.sessionDao().getAllSessionIds().toString());
        List<User> usersFromSession = db.sessionDao().getUsersBySessionId(sessionId);
        Log.i("----------Users in session: ", usersFromSession.toString());

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