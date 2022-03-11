package com.example.bofteam24;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView studentView;

    public MockMessageListener(Context context, RecyclerView studentView) {
        super();
        this.context = context;
        db = AppDatabase.singleton(context);
        user = UserSelf.getInstance(context);
        this.studentView = studentView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onFound(@NonNull Message message) {
        String messageString = new String(message.getContent());
        Log.i(TAG, "-------- Found message: " + "\n" + messageString);
        Toast.makeText(context, "Found message: " + messageString, Toast.LENGTH_SHORT).show();
        // Log.d("------------messageString ", messageString);
        messageString = messageString.trim();
        String[] csvInfoDivided = ParseUtils.cleanCVSInput(messageString);

        String userIDString = ParseUtils.getUserId(csvInfoDivided);
        String firstName = ParseUtils.getUserFirstName(csvInfoDivided);
        String photoURL = ParseUtils.getUserPhotoURL(csvInfoDivided);

        ArrayList<String> allCoursesString = ParseUtils.getAllCoursesInfo(csvInfoDivided);
        // ParseUtils.printAllCourses(allCoursesString);

        boolean wave = false;
        if (allCoursesString.get(allCoursesString.size() - 1).contains("_")) {
            String userIdOfWavie = allCoursesString.get(allCoursesString.size() - 1).split("_")[0];
            String waveString = allCoursesString.get(allCoursesString.size() - 1).split("_")[1]; // String waveString = "wave"
            allCoursesString.remove(allCoursesString.size()-1);
            wave = true;
        }

        Log.d("------------------ user ID when creating user ", userIDString);
        User otherUser = db.userDao().getUserWithId(userIDString);

        // user does not exist in database
        if(otherUser == null) {
            List<CourseRoom> myCourses = db.courseDao().getForUser(user.getUserId());

            int sameNumCourses = ParseUtils.getSameNumCourses1(myCourses, allCoursesString);

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
        else { // user already exists in database
            Log.d("------------------ user with ^ ID DOES exist in DB ", "...");
            List<CourseRoom> otherUserPrevCourses = db.courseDao().getForUser(otherUser.getUserId());
            List<String> otherUserPrevCoursesString = new ArrayList<>();

            for (CourseRoom cr : otherUserPrevCourses) {
                // Log.d("------------------ other user prev courses ", cr.getCourseName());
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

            int sameNumCourses = ParseUtils.getSameNumCourses2(myCourses, updatedOtherUserCourses);

            if (!photoURL.equals(otherUser.getPhotoUrl())) {
                otherUser.setPhotoUrl(photoURL);
                db.userDao().updateUserPhoto(otherUser.getUserId(), photoURL);
            }

            if (sameNumCourses == 0) {
                db.courseDao().deleteUserCourses(otherUser.getUserId());
                db.userDao().delete(otherUser);
            }
            else if (sameNumCourses != (otherUser.getNumOfSameCourses())) {
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
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(StudentsListActivity.users);
        studentView.setAdapter(studentViewAdapter);
    }

    @Override
    public void onLost(@NonNull Message message) {
        Log.i(TAG, "-------- Lost message: " + "\n" + new String(message.getContent()));
    }
}
