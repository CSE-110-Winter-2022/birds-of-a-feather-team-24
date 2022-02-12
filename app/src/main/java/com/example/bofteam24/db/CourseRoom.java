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

    @PrimaryKey(autoGenerate = true)
    private int courseId = 0;

    @ColumnInfo
    private String userId;

    @ColumnInfo
    private String courseName;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
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

    //courseId, userId1, "CSE 12 FALL 2019"
    //courseId, userId1, "CSE 12 FALL 2019"
    //courseId, userId, "CSE 12 FALL 2019"
    //courseId, userId, "CSE 12 FALL 2019"

}
