package com.example.bofteam24.db;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithCourses {
    @Embedded
    public User user;

    @Relation(parentColumn = "userId",
            entityColumn = "courseId",
            entity = CourseRoom.class,
            projection = {"courseName"})
    public List<String> courses;

    public String getId() { return this.user.getUserId(); }

    public String getName() { return this.user.getName(); }

    public List<String> getCourses() { return courses; }
}