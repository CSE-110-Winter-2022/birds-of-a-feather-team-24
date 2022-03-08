package com.example.bofteam24;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockMessageListener extends MessageListener {

    private static final String TAG = "BoF-Team24";

    private Context context;
    private AppDatabase db;
    private User user;
    public MockMessageListener(Context context) {
        this.context = context;
        db = AppDatabase.singleton(context);
        user = UserSelf.getInstance(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onFound(@NonNull Message message) {
        String messageString = new String(message.getContent());
        Log.d(TAG, "Found message: " + messageString);
        // Log.d("------------messageString ", messageString);
        messageString = messageString.trim();
        String[] csvInfoDivided = ParseUtils.cleanCVSInput(messageString);
        String[] firstNameAndPhotoUrl = ParseUtils.getFirstNameAndUrl(csvInfoDivided);
        String firstName = firstNameAndPhotoUrl[0];
        String photoURL = firstNameAndPhotoUrl[1];
        ArrayList<String> allCoursesString = ParseUtils.getAllCoursesInfo(csvInfoDivided);
        // allCoursesInfoString = ["2022 WI CSE 110", "2021 WI CSE 11", "2022 SP MMW 121", etc]

        // this needs to be actual courses
        // String[] myCourses = {"2022 WI CSE 110", "2021 FA CSE 140", "2021 FA CSE 101"}; //random
        List<CourseRoom> myCourses = db.courseDao().getForUser(user.getUserId());

        int sameCourses = 0;
        for(CourseRoom course : myCourses) {
            String myCourse = course.toMockString();
            for(String otherCourse : allCoursesString) {
                Log.i("PAIRS", myCourse + ";" + otherCourse);
                if (myCourse.equals(otherCourse)) {
                    sameCourses+=1;
                }
            }
        }

        if (sameCourses == 0) { return; }

        // THIS IS ONLY FOR MOCKING
        String userIDString = UUID.randomUUID().toString();
        Log.d("------------------ user ID when creating user ", userIDString);
        User otherUser = new User(userIDString, firstName, photoURL, sameCourses, false);
        db.userDao().insert(otherUser);

        for (String oneCourse : allCoursesString) {
            int courseID = (int) (Math.random() * 10000);
            CourseRoom course = new CourseRoom(courseID, userIDString, oneCourse);
            db.courseDao().insert(course);
        }
        StudentsListActivity.allCoursesInfo = db.courseDao().getAll();
        StudentsListActivity.users = db.userDao().getOthers(user.getUserId());
    }

    @Override
    public void onLost(@NonNull Message message) {
        Log.d(TAG, "Lost message: " + new String(message.getContent()));
    }
}
