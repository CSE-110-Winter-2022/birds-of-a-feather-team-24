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

import java.lang.reflect.Array;
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
        Log.i(TAG, "Found message: " + messageString);
        // Log.d("------------messageString ", messageString);
        messageString = messageString.trim();
        String[] csvInfoDivided = ParseUtils.cleanCVSInput(messageString);
        for(int i = 0; i < csvInfoDivided.length; i++) {
            String c = csvInfoDivided[i];
            Log.d(i + ") ---------------- in MockMessageListener csvInfoDivided[" + i + "]", c);
        }

        String userIDString = ParseUtils.getUserId(csvInfoDivided);
        String firstName = ParseUtils.getUserFirstName(csvInfoDivided);
        String photoURL = ParseUtils.getUserPhotoURL(csvInfoDivided);

        Log.d("---------------- in MockMessageListener userIDString", userIDString);
        Log.d("---------------- in MockMessageListener firstName", firstName);
        Log.d("---------------- in MockMessageListener photoURL", photoURL);
        ArrayList<String> allCoursesString = ParseUtils.getAllCoursesInfo(csvInfoDivided);
        Log.d("---------------- getting all courses info", "...");
        ParseUtils.printAllCourses(allCoursesString);

        boolean wave = false;
        if (allCoursesString.get(allCoursesString.size() - 1).contains("_")) {
            String userIdOfWavie = allCoursesString.get(allCoursesString.size() - 1).split("_")[0];
            String waveString = allCoursesString.get(allCoursesString.size() - 1).split("_")[1]; // String waveString = "wave"
            allCoursesString.remove(allCoursesString.size()-1);
            wave = true;
        }

        Log.d("------------------ user ID when creating user ", userIDString);
        User otherUser = db.userDao().getUserWithId(userIDString);

        if(otherUser == null) {
            List<CourseRoom> myCourses = db.courseDao().getForUser(user.getUserId());

            int sameNumCourses = 0;
            for(CourseRoom course : myCourses) {
                String myCourse = course.toMockString();
                for(String otherCourse : allCoursesString) {
                    Log.i("PAIRS", myCourse + ";" + otherCourse);
                    if (myCourse.equals(otherCourse)) {
                        sameNumCourses+=1;
                    }
                }
            }

            if (sameNumCourses == 0) { return; }

            Log.d("------------------ user with ^ ID does NOT exist in DB ", "...");
            otherUser = new User(userIDString, firstName, photoURL, sameNumCourses);
            db.userDao().insert(otherUser);

            for (String oneCourse : allCoursesString) {
                // int courseID = (int) (Math.random() * 10000);
                CourseRoom course = new CourseRoom(0, userIDString, oneCourse);
                db.courseDao().insert(course);
            }
        }
        else {
            Log.d("------------------ user with ^ ID DOES exist in DB ", "...");
            List<CourseRoom> otherUserPrevCourses = db.courseDao().getForUser(otherUser.getUserId());
            List<String> otherUserPrevCoursesString = new ArrayList<>();

            for (CourseRoom cr : otherUserPrevCourses) {
                Log.d("------------------ other user prev courses ", cr.getCourseName());
                otherUserPrevCoursesString.add(cr.getCourseName());
            }

            List<String> addedCoursesString = new ArrayList<String>();
            List<String> removedCoursesString = new ArrayList<String>();

            for (String course : allCoursesString) {
                if (!otherUserPrevCoursesString.contains(course)) {
                    addedCoursesString.add(course);
                }
            }

            for (String course : addedCoursesString) {
                CourseRoom courseRoom = new CourseRoom(0, otherUser.getUserId(), course);
                db.courseDao().insert(courseRoom);
            }

            for (String course : otherUserPrevCoursesString) {
                if (!allCoursesString.contains(course)) {
                    removedCoursesString.add(course);
                }
            }

            for (String courseName : removedCoursesString) {
                CourseRoom courseRoom = db.courseDao().getSpecificCourse(otherUser.getUserId(), courseName);
                db.courseDao().delete(courseRoom);
            }

            List<CourseRoom> updatedOtherUserCourses = db.courseDao().getForUser(otherUser.getUserId());
            List<CourseRoom> myCourses = db.courseDao().getForUser(user.getUserId());

            int sameNumCourses = 0;
            for(CourseRoom course : myCourses) {
                String myCourse = course.toMockString();
                for(CourseRoom otherCourseRoom : updatedOtherUserCourses) {
                    String otherCourse = otherCourseRoom.getCourseName();
                    Log.i("PAIRS", myCourse + ";" + otherCourse);
                    if (myCourse.equals(otherCourse)) {
                        sameNumCourses+=1;
                    }
                }
            }

            if (!photoURL.equals(otherUser.getPhotoUrl())) {
                otherUser.setPhotoUrl(photoURL);
                db.userDao().updateUserPhoto(otherUser.getUserId(), photoURL);
            }

            if (sameNumCourses != (otherUser.getNumOfSameCourses())) {
                otherUser.setNumOfSameCourses(sameNumCourses); // 1
                db.userDao().updateNumCourses(otherUser.getUserId(), sameNumCourses);
            }

            if (!otherUser.getWave() && wave) {
                otherUser.setWave(true);
                db.userDao().updateUserWave(otherUser.getUserId(), true);
            }

            otherUser = db.userDao().getUserWithId(otherUser.getUserId());

        }

        StudentsListActivity.allCoursesInfo = db.courseDao().getAll();
        StudentsListActivity.users = db.userDao().getOthers(user.getUserId());
    }

    @Override
    public void onLost(@NonNull Message message) {
        Log.i(TAG, "Lost message: " + new String(message.getContent()));
    }
}
