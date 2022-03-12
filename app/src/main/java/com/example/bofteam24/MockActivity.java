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
import com.google.android.gms.nearby.messages.MessageListener;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MockActivity extends AppCompatActivity {

    // public static MessageListener messageListener;
    public static Message mMessage;
    public static ArrayList<String> incomingMessagesString = new ArrayList<>();
    // private ArrayList<Message> incomingMessages;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
        db = AppDatabase.singleton(this);
        // incomingMessagesString = new ArrayList<>();
        // if(messageListener == null) messageListener = new MockMessageListener(getApplicationContext());
    }

    public void onEnterClick(View view) {
        EditText editText = this.findViewById(R.id.edit_text);
        String csvInfo = editText.getText().toString();
        Log.d("--------------- Entered Mock Info is ", csvInfo);

        if (!csvInfo.equals("")) {
            incomingMessagesString.add(csvInfo);
        }

        editText.setText("");
    }

    public void onBackClick(View view) {
        Log.d("--------------- incomingMessagesString size ", Integer.toString(incomingMessagesString.size()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // ------- comment everything from here ----------------------
//        if(this.db != null) {
//            this.db.courseDao().deleteAll();
//            this.db.userDao().deleteAll();
//        }
        // ---------------------- to here to retain prev users ----------------------
        super.onDestroy();
    }
}