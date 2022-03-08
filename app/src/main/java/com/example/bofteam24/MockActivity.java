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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MockActivity extends AppCompatActivity {

    public static MessageListener messageListener;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
        db = AppDatabase.singleton(this);

        if(messageListener == null) messageListener = new MockMessageListener(getApplicationContext());

        Nearby.getMessagesClient(this).subscribe(messageListener);

        String userId = UserSelf.getInstance(this).getUserId();
        String test = "Bill,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n" +
                "2021,FA,CSE,210\n" +
                "2022,WI,CSE,110\n" +
                "2022,SP,CSE,110";
        User user = db.userDao().getUserWithId(userId);
        List<CourseRoom> courses = db.courseDao().getForUser(userId);
        String userName = user.getName();
        String photoURL = user.getPhotoUrl();
        List<String> stringCourses = new ArrayList<String>();

        for(int i = 0; i < courses.size(); i++) {
            CourseRoom course = courses.get(i);
            String stringCourse = course.toMockString();
            stringCourse = stringCourse.replaceAll(" ", ",");
            if (i != courses.size()-1) { stringCourse += "\n"; }
            stringCourses.add(stringCourse);
        }

        StringBuilder myMessage = new StringBuilder(userName + ",,,\n" + photoURL + ",,,\n");

        for(String course : stringCourses) {
            myMessage.append(course);//.append("\n");
        }

//        Log.d("-------------------- test message ", "Bill,,,\n" +
//                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n" +
//                "2021,FA,CSE,210\n" +
//                "2022,WI,CSE,110\n" +
//                "2022,SP,CSE,110");

        Log.d("---------------------- myMessage ", myMessage.toString());

        Nearby.getMessagesClient(this).publish(new Message(myMessage.toString().getBytes()));

    }

    public void onEnterClick(View view) {
        EditText editText = this.findViewById(R.id.edit_text);
        String csvInfo = editText.getText().toString();
        StudentsListActivity.cameFromMock = true;
        if (!csvInfo.equals("")) {
            Message mMessage = new Message(csvInfo.getBytes());
            Nearby.getMessagesClient(this).subscribe(messageListener);
            messageListener.onFound(mMessage);
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
//        if(this.db != null) {
//            this.db.courseDao().deleteAll();
//            this.db.userDao().deleteAll();
//        }
        // ---------------------- to here to retain prev users ----------------------
        super.onDestroy();
    }
}