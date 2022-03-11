package com.example.bofteam24.Sorting;

import com.example.bofteam24.Course;
import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class RecentCommonalitySort extends SortingStrategy {

    static final HashMap<String, Integer> quarter_to_num = new HashMap<>();
    static {
        quarter_to_num.put("WINTER", 0);
        quarter_to_num.put("SPRING", 1);
        quarter_to_num.put("SUMMER SESSION 1", 2);
        quarter_to_num.put("SUMMER SESSION 2", 2);
        quarter_to_num.put("SPECIAL SUMMER SESSION", 2);
        quarter_to_num.put("FALL", 3);
    }

    public RecentCommonalitySort(User user, AppDatabase db) {
        super(user, db);
    }


    public double calculateCourseScore(CourseRoom course) {
        int age;
        int score;
        String courseQuarter;
        int courseYear;
        String[] course_desc = course.getCourseName().split(" ");

        if (course_desc.length > 4) {
            courseQuarter = course_desc[2] + " " + course_desc[3] + " " + course_desc[4];
            courseYear = Integer.parseInt(course_desc[5]);
        } else {
            courseQuarter = course_desc[2];
            courseYear = Integer.parseInt(course_desc[3]);
        }

        if (courseQuarter.equals(currentQuarter) && courseYear == currentYear) {
            return 0;
        } else {
            age = calculateAge(courseQuarter, courseYear);
        }

        if (age >= 4) {
            return 1;
        }
        return 5-age;
    }

    public int calculateAge(String quarter, int year) {

        return (currentYear - year) * 4 +
                quarter_to_num.get(currentQuarter) - quarter_to_num.get(quarter) - 1;
    }
}
