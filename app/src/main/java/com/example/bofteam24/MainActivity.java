package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bofteam24.db.AppDatabase;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(UserSelf.getInstance(this).getUserId().equals("")) {
            Intent loginAct = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginAct);
        }
        db = AppDatabase.singleton(this);
    }

    @Override
    protected void onDestroy() {
        // ------- comment everything from here ----------------------
        if(this.db != null) {
            String userId = UserSelf.getInstance(this).getUserId();
            this.db.courseDao().deleteOthers(userId);
            this.db.userDao().deleteOthers(userId);
        }
        // ---------------------- to here to retain prev users ----------------------
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

    public void onGoToProfileClicked(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        String userId = UserSelf.getInstance(this).getUserId();
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
}
