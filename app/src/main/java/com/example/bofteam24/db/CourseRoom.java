package com.example.bofteam24.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class CourseRoom {

    public CourseRoom(int courseId, int userId, String courseName) {
        this.courseId = courseId;
        this.userId = userId;
        this.courseName = courseName;
    }

    public String toString() {
        return this.courseName;
    }

    @PrimaryKey
    @ColumnInfo
    public int courseId;

    @ColumnInfo
    public int userId;

    @ColumnInfo
    public String courseName;
}
