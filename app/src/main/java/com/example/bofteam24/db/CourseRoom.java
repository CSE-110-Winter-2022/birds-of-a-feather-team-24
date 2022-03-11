package com.example.bofteam24.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class CourseRoom {

    public CourseRoom(int courseId, String userId, String courseName) {
        this.courseId = courseId;
        this.userId = userId;
        this.courseName = courseName;
    }

    public String toString() {
        return this.courseName;
    }

    // String[] myCourses = {"2022 WI CSE 110", "2021 FA CSE 140", "2021 FA CSE 101"}; //random
    public String toMockString() {
        String[] details = courseName.toUpperCase().split(" ");
        //courseId, userId, "CSE 12 FALL 2019"
        String subject = details[0];
        String num = details[1];
        String quarter = details[2];
        String year = details[3];
        String quarterAbbrev = "";
        if(quarter.equals("FALL")) {
            quarterAbbrev = "FA";
        }
        else if(quarter.equals("WINTER")) {
            quarterAbbrev = "WI";
        }
        else if(quarter.equals("SPRING")) {
            quarterAbbrev = "SP";
        }
        else if(quarter.equals("SUMMER SESSION 1")) {
            quarterAbbrev = "SS1";
        }
        else if(quarter.equals("SUMMER SESSION 2")) {
            quarterAbbrev = "SS2";
        }
        else { // SPECIAL SUMMER SESSION
            quarterAbbrev = "SSS";
        }
        return String.format("%s %s %s %s", year, quarterAbbrev, subject, num);
    }

    @PrimaryKey(autoGenerate = true) // auto generate a unique ID for courseID
    private int courseId = 0;   // courseID is just there as if it is a row number

    @ColumnInfo
    private String userId;

    @ColumnInfo
    private String courseName;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean equals(CourseRoom other) { return other != null &&
            this.courseName.equals(other.courseName); }

    //courseId, userId1, "CSE 12 FALL 2019"
    //courseId, userId1, "CSE 12 FALL 2019"
    //courseId, userId, "CSE 12 FALL 2019"
    //courseId, userId, "CSE 12 FALL 2019"

}