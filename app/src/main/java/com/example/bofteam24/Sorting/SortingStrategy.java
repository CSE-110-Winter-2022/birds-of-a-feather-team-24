package com.example.bofteam24.Sorting;

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
        int score = 0;

        for(CourseRoom course : courses) {
            for (CourseRoom others_course: others_courses) {
                if (course.equals(others_course)) {
                    score += calculateCourseScore(course);
                }
            }
        }
        return score;
    }

    public abstract double calculateCourseScore(CourseRoom course);
}
