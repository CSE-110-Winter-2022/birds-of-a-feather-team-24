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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LoadedSessionActivity extends AppCompatActivity {

    AppDatabase db;
    private List<User> usersWithoutDuplicates;
    Spinner sortSpinner;

    private List<User> getNoDuplicates(List<User> fromSession) {
        List<User> usersWithoutDuplicates = new ArrayList<>();
        HashSet<String> found = new HashSet<>();

        for (User user: fromSession) {
            if (!found.contains(user.getUserId())) {
                found.add(user.getUserId());
                usersWithoutDuplicates.add(user);
            }
        }
        return usersWithoutDuplicates;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_session);
        db = AppDatabase.singleton(this);

        Long sessionId = getIntent().getLongExtra("sessionId", -1);
        Log.d("------------Opening sessionId: " , sessionId.toString());
        Log.d("-----------All sessionId in database: ", db.sessionDao().getAllSessionIds().toString());
        List<User> usersFromSession = db.sessionDao().getUsersBySessionId(sessionId);
        this.usersWithoutDuplicates = getNoDuplicates(usersFromSession);
        for(User user : usersFromSession) {
            Log.d(ParseUtils.TAG, "--------- User in session ID: " + user.getUserId());
            Log.d(ParseUtils.TAG, "--------- User in session NAME: " + user.getName());
        }

        for(User user : usersWithoutDuplicates) {
            Log.d(ParseUtils.TAG, "usersWithoutDuplicates");
            Log.d(ParseUtils.TAG, "--------- User in session ID: " + user.getUserId());
            Log.d(ParseUtils.TAG, "--------- User in session NAME: " + user.getName());
        }
        Log.d("----------Users in session: ", usersFromSession.toString());

        this.sortSpinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SortUsers.byStrategy(LoadedSessionActivity.this, sortSpinner, usersWithoutDuplicates);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                SortUsers.byStrategy(LoadedSessionActivity.this, sortSpinner, usersWithoutDuplicates);
            }
        });

        SortUsers.byStrategy(LoadedSessionActivity.this, sortSpinner, usersWithoutDuplicates);

        RecyclerView studentView = findViewById(R.id.session_student_view);
        LinearLayoutManager studentLayoutManager = new LinearLayoutManager(this);
        studentView.setLayoutManager(studentLayoutManager);
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(usersWithoutDuplicates);
        studentView.setAdapter(studentViewAdapter);
    }

    public void onBackClicked(View view) {
        finish();
    }

    private void sortUsers() {
        SortUsers.byStrategy(this, sortSpinner, this.usersWithoutDuplicates);
    }

    public void onLoadSortClick(View view) {
        sortUsers();

        RecyclerView studentView = findViewById(R.id.session_student_view);
        LinearLayoutManager studentLayoutManager = new LinearLayoutManager(this);
        studentView.setLayoutManager(studentLayoutManager);
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(usersWithoutDuplicates);
        studentView.setAdapter(studentViewAdapter);
    }
}