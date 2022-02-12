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

    public int getId() { return this.user.userId; }

    public String getName() { return this.user.name; }

    public List<String> getCourses() { return this.courses; }
}
