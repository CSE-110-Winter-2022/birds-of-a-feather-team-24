package com.example.bofteam24.sorting;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;

import java.util.HashMap;

public class ClassSizeSort extends SortingStrategy {

    static final HashMap<String, Double> size_to_weights = new HashMap<>();
    static {
//        size_to_weights.put("Tiny (less than 40)", 1.00);
//        size_to_weights.put("Small (40-75)", 0.33);
//        size_to_weights.put("Medium (75-150)", 0.18);
//        size_to_weights.put("Large(150-250)", 0.10);
//        size_to_weights.put("Huge (250-400)", 0.06);
//        size_to_weights.put("Gigantic (400+)", 0.03);
        size_to_weights.put("Tiny", 1.00);
        size_to_weights.put("Small", 0.33);
        size_to_weights.put("Medium", 0.18);
        size_to_weights.put("Large", 0.10);
        size_to_weights.put("Huge", 0.06);
        size_to_weights.put("Gigantic", 0.03);
    }

    public ClassSizeSort(User user, AppDatabase db) {
        super(user, db);
    }

    @Override
    public double calculateCourseScore(CourseRoom course) {
        return size_to_weights.get(course.getCourseSize());
    }
}
