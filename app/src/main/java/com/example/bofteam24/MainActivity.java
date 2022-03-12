package com.example.bofteam24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.Nearby;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;

//    private void setSameNumCourses() {
//        User me = UserSelf.getInstance(this);
//        List<User> otherUsers = db.userDao().getOthers(me.getUserId());
//        List<CourseRoom> myCourses = db.courseDao().getForUser(me.getUserId());
//
//        for(User otherUser : otherUsers) {
//            List<CourseRoom> otherCoursesRoom = db.courseDao().getForUser(otherUser.getUserId());
//            int sameNumCourses = 0;
//            for(CourseRoom course : myCourses) {
//                String myCourse = course.toMockString();
//                for(CourseRoom otherCourseRoom : otherCoursesRoom) {
//                    String otherCourse = otherCourseRoom.getCourseName();
//                    // Log.i("PAIRS", myCourse + ";" + otherCourse);
//                    if (myCourse.equals(otherCourse)) {
//                        sameNumCourses+=1;
//                    }
//                }
//            }
//            if (otherUser.getNumOfSameCourses() != sameNumCourses) {
//                if (sameNumCourses == 0) {
//                    db.courseDao().deleteUserCourses(otherUser.getUserId());
//                    db.userDao().delete(otherUser);
//                }
//                else {
//                    otherUser.setNumOfSameCourses(sameNumCourses);
//                    db.userDao().updateNumCourses(otherUser.getUserId(), sameNumCourses);
//                }
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(UserSelf.getInstance(this).getUserId().equals("")) {
            Intent loginAct = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginAct);
        }
        db = AppDatabase.singleton(this);
        List<String> allSessionNames = db.sessionDao().getAll();
        Log.d(ParseUtils.TAG, "----------- In Main db.sessionDao().getAll() size: "
                + Integer.toString(db.sessionDao().getAll().size()));

        if (!allSessionNames.isEmpty()) {
            //set session elements visible
            Spinner sessionSpinner = findViewById(R.id.session_spinner);
            TextView session_label = findViewById(R.id.choose_session_label);
            Button load_session_button = findViewById(R.id.go_to_past_session_button);

            sessionSpinner.setVisibility(View.VISIBLE);
            session_label.setVisibility(View.VISIBLE);
            load_session_button.setVisibility(View.VISIBLE);
            //populate session_spinner with all saved sessions
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item);
            adapter.addAll(allSessionNames);
            sessionSpinner.setAdapter(adapter);
        }

        String userId = UserSelf.getInstance(this).getUserId();
        Log.d(ParseUtils.TAG, " --------- MY OWN UUID = " + userId);
        // setSameNumCourses();
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

    public void onFavClick(View view) {
        Intent intent = new Intent(this, FavStudentsListActivity.class);
        startActivity(intent);
    }

    public void onLoadClicked(View view) {
        Spinner loaded_sessions_spinner = findViewById(R.id.session_spinner);
        String selected = loaded_sessions_spinner.getSelectedItem().toString();

        List<Long> sessionId = db.sessionDao().getSessionIdForSession(selected);

        Intent intent = new Intent(this, LoadedSessionActivity.class);
        intent.putExtra("sessionId", sessionId.get(0));
        startActivity(intent);
    }
}
