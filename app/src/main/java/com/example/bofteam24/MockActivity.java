package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MockActivity extends AppCompatActivity {

    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
        db = AppDatabase.singleton(this);
    }

    public void onEnterClick(View view) {
        EditText editText = this.findViewById(R.id.edit_text);
        String csvInfo = editText.getText().toString();
        StudentsListActivity.cameFromMock = true;
        if (!csvInfo.equals("")) {
            Message mMessage = new Message(csvInfo.getBytes());
            Nearby.getMessagesClient(this).subscribe(StudentsListActivity.messageListener);
            StudentsListActivity.messageListener.onFound(mMessage);
        }

        editText.setText("");
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // ------- comment everything from here ----------------------
        if (StudentsListActivity.db != null) {
            StudentsListActivity.db.courseDao().deleteAll();
            StudentsListActivity.db.userDao().deleteAll();
        }
        if(this.db != null) {
            this.db.courseDao().deleteAll();
            this.db.userDao().deleteAll();
        }
        // ---------------------- to here to retain prev users ----------------------
        super.onDestroy();
    }
}