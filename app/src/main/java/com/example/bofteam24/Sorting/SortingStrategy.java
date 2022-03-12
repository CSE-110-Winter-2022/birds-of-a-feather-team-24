package com.example.bofteam24.Sorting;

import android.util.Log;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;

import java.util.Comparator;
import java.util.List;

public abstract class SortingStrategy implements Comparator<User> {

    User user;
    AppDatabase db;
    List<CourseRoom> courses;
    static final String currentQuarter = "WINTER";
    static final int currentYear = 2022;

    public SortingStrategy(User user, AppDatabase db) {
        this.user = user;
        this.db = db;
        this.courses = db.courseDao().getForUser(user.getUserId());
    }

//    @Override
//    public int compare(User u1, User u2) {
//        return calculateScore(u2) - calculateScore(u1);
//    }

    @Override
    public int compare(User u1, User u2) {
        if (calculateScore(u2) > calculateScore(u1)) {
            return 1;
        }
        if (calculateScore(u2) < calculateScore(u1)) {
            return -1;
        }
        return 0;
    }

    public double calculateScore(User other) {
        String id = other.getUserId();
        List<CourseRoom> others_courses = db.courseDao().getForUser(id);
        courses = db.courseDao().getForUser(user.getUserId());
        int score = 0;

        Log.i("----------CalculateScore ", "size of others_courses = " + others_courses.size());
        Log.i("----------CalculateScore ", "size of my_courses = " + courses.size());
        Log.i("----------CalculateScore ", "my id = " + user.getUserId());

        for(CourseRoom course : courses) {
            System.out.println("----------SortingStrategy: in for-loop");
            for (CourseRoom others_course: others_courses) {
                if (course.equals(others_course)) {
                    score += calculateCourseScore(course);
                    Log.i("----------SortingStrategy: ", "score is now " + score);
                }
            }
        }
        return score;
    }

    public abstract double calculateCourseScore(CourseRoom course);
}
