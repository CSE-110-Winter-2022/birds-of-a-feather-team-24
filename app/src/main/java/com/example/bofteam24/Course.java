package com.example.bofteam24;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private int year;
    private String quarter;
    private String subject;
    private String courseNumber;

    public Course(int year, String quarter, String subject, String courseNumber) {
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNumber = courseNumber;
    }

    public void create(Context ctx) {
        SharedPreferences spfs = ctx.getSharedPreferences(
                ctx.getString(R.string.course_prefs),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = spfs.edit();
        Gson gson = new Gson();

        Course[] oldList = getAll(ctx);
        Course[] newList = new Course[oldList.length + 1];
        System.arraycopy(oldList, 0, newList, 0, oldList.length);
        newList[oldList.length] = this;

        String json = gson.toJson(newList);
        editor.putString(ctx.getString(R.string.course_list), json);
        editor.commit();
    }

    public static Course[] getAll(Context ctx) {
        SharedPreferences spfs = ctx.getSharedPreferences(
                ctx.getString(R.string.course_prefs),
                Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = spfs.getString(ctx.getString(R.string.course_list), "[]");
        Course[] courses = gson.fromJson(json, Course[].class);
        return courses;
    }

    public String toString() {
        return String.format("%s %s %s %d", subject, courseNumber, quarter, year);
    }

}
