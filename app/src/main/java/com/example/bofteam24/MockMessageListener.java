package com.example.bofteam24;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private void userNotInDbTODO(List<String> allCoursesString, String userIDString,
                             String firstName, String photoURL) {

        List<CourseRoom> myCourses = db.courseDao().getForUser(user.getUserId());

        int sameNumCourses = ParseUtils.getSameNumCourses1(myCourses, allCoursesString);

        if (sameNumCourses == 0) { return; }

        Log.d("------------------ user with ^ ID does NOT exist in DB ", "...");
        User otherUser = new User(userIDString, firstName, photoURL, sameNumCourses);
        db.userDao().insert(otherUser);

        for (String oneCourse : allCoursesString) {

            //CID, UID, "2022 WI CSE 110 Small"
            String[] courseSplit = oneCourse.split(" ");
            String courseName = courseSplit[0] + " " + courseSplit[1] + " " + courseSplit[2] + " " + courseSplit[3];
            String courseSize = courseSplit[4];

            CourseRoom course = new CourseRoom(null, userIDString, courseName, courseSize);
            String courseStringId = Integer.toString(course.getCourseId());
            Log.d("----------------- new course added Id ", courseStringId);
            db.courseDao().insert(course);
        }
    }

    private List<String> courseRoomToStringList(List<CourseRoom> courseRoomArrList) {
        List<String> stringArrList = new ArrayList<>();
        for (CourseRoom cr : courseRoomArrList) {
            // Log.d("------------------ other user prev courses ", cr.getCourseName());
            stringArrList.add(cr.getCourseName());
        }
        return stringArrList;
    }

    List<String> getNewlyAddedCourses(List<String> allCoursesString,
                                          List<String> otherUserPrevCoursesString) {
        List<String> addedCoursesString = new ArrayList<String>();
        for (String course : allCoursesString) {
            if (!otherUserPrevCoursesString.contains(course)) {
                addedCoursesString.add(course);
            }
        }
        return addedCoursesString;
    }

    List<String> getNewlyRemovedCourses(List<String> allCoursesString,
                                        List<String> otherUserPrevCoursesString) {

        List<String> removedCoursesString = new ArrayList<String>();
        for (String course : otherUserPrevCoursesString) {
            if (!allCoursesString.contains(course)) {
                removedCoursesString.add(course);
            }
        }
        return  removedCoursesString;
    }

    private void addNewlyAddedCoursesToDb(List<String> addedCoursesString,
                                          User otherUser) {

        for (String course : addedCoursesString) {
            String[] courseSplit = course.split(" ");
            String courseName = courseSplit[0] + " " + courseSplit[1] + " " + courseSplit[2] + " " + courseSplit[3];
            String courseSize = courseSplit[4];
            CourseRoom courseRoom = new CourseRoom(null, otherUser.getUserId(), courseName, courseSize);
            db.courseDao().insert(courseRoom);
        }
    }

    private void removeNewlyRemovedCoursesFromDb(List<String> removedCoursesString,
                                                 User otherUser) {

        for (String courseName : removedCoursesString) {
            CourseRoom courseRoom = db.courseDao().getSpecificCourse(otherUser.getUserId(), courseName);
            db.courseDao().delete(courseRoom);
        }
    }

    private int getSameNumOfCourses(User otherUser) {
        List<CourseRoom> updatedOtherUserCourses = db.courseDao().getForUser(otherUser.getUserId());
        List<CourseRoom> myCourses = db.courseDao().getForUser(user.getUserId());

        return ParseUtils.getSameNumCourses2(myCourses, updatedOtherUserCourses);
    }

    private boolean getWaveInfo(List<String> allCoursesString) {

        boolean wave = false;
        if (allCoursesString.get(allCoursesString.size() - 1).contains("_")) {
            String userIdOfWavie = allCoursesString.get(allCoursesString.size() - 1).split("_")[0];
            String waveString = allCoursesString.get(allCoursesString.size() - 1).split("_")[1]; // String waveString = "wave"
            allCoursesString.remove(allCoursesString.size()-1);
            wave = true;
        }
        return wave;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onFound(@NonNull Message message) {
        String messageString = new String(message.getContent());
        Log.i(TAG, "-------- Found message: " + "\n" + messageString);
        // Toast.makeText(context, "Found message: " + messageString, Toast.LENGTH_SHORT).show();
        // Log.d("------------messageString ", messageString);
        messageString = messageString.trim();
        String[] csvInfoDivided = ParseUtils.cleanCSVInput(messageString);

        String userIDString = ParseUtils.getUserId(csvInfoDivided);
        String firstName = ParseUtils.getUserFirstName(csvInfoDivided);
        String photoURL = ParseUtils.getUserPhotoURL(csvInfoDivided);
        Toast.makeText(context, "Found message: " + firstName, Toast.LENGTH_SHORT).show();

        ArrayList<String> allCoursesString = ParseUtils.getAllCoursesInfo(csvInfoDivided);
        // ParseUtils.printAllCourses(allCoursesString);

        boolean wave = getWaveInfo(allCoursesString);

        Log.d("------------------ user ID when creating user ", userIDString);
        User otherUser = db.userDao().getUserWithId(userIDString);

        // user does not exist in database
        if(otherUser == null) {
            Log.d("------------------ user with ^ ID does NOT exist in DB ", "...");
            userNotInDbTODO(allCoursesString, userIDString, firstName, photoURL);
        }
        else { // user already exists in database
            Log.d("------------------ user with ^ ID DOES exist in DB ", "...");

            List<CourseRoom> otherUserPrevCourses = db.courseDao().getForUser(otherUser.getUserId());
            List<String> otherUserPrevCoursesString = courseRoomToStringList(otherUserPrevCourses);

            List<String> addedCoursesString = getNewlyAddedCourses(allCoursesString,
                    otherUserPrevCoursesString);
            addNewlyAddedCoursesToDb(addedCoursesString, otherUser);

            List<String> removedCoursesString = getNewlyRemovedCourses(allCoursesString,
                    otherUserPrevCoursesString);;
            removeNewlyRemovedCoursesFromDb(removedCoursesString, otherUser);

            int sameNumCourses = getSameNumOfCourses(otherUser);

            if (!photoURL.equals(otherUser.getPhotoUrl())) {
                otherUser.setPhotoUrl(photoURL);
                db.userDao().updateUserPhoto(otherUser.getUserId(), photoURL);
            }

            if (sameNumCourses == 0) {
                db.courseDao().deleteUserCourses(otherUser.getUserId());
                String deletedUserName = otherUser.getName();
                db.userDao().delete(otherUser);
                List<String> oldCsvInputs = new ArrayList<>();
                for (String csvInput : MockActivity.incomingMessagesString) {
                    oldCsvInputs.add(csvInput);
                }
                MockActivity.incomingMessagesString = new ArrayList<>();
                for(String csvInput : oldCsvInputs) {
                    String userName = csvInput.split("\n")[1];
                    if (! userName.equals(deletedUserName)) {
                        MockActivity.incomingMessagesString.add(csvInput);
                    }
                }
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

        Collections.sort(StudentsListActivity.users, Comparator.comparing(User::getNumOfSameCourses));
        Collections.reverse(StudentsListActivity.users);

        LinearLayoutManager studentLayoutManager = new LinearLayoutManager(this.context);
        studentView.setLayoutManager(studentLayoutManager);
        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(StudentsListActivity.users);
        studentView.setAdapter(studentViewAdapter);

//        StudentViewAdapter studentViewAdapter = new StudentViewAdapter(StudentsListActivity.users);
//        studentView.setAdapter(studentViewAdapter);
    }

    @Override
    public void onLost(@NonNull Message message) {
        Log.i(TAG, "-------- Lost message: " + "\n" + new String(message.getContent()));
    }
}
