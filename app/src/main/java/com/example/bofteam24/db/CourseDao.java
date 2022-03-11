package com.example.bofteam24.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CourseDao {
    @Transaction
    @Query("SELECT * FROM courses where userId=:userId")
    List<CourseRoom> getForUser(String userId);

    @Transaction
    @Query("SELECT * FROM courses")
    List<CourseRoom> getAll();

    @Transaction
    @Query("SELECT * FROM courses where courseId=:courseId")
    CourseRoom get(int courseId);

    //add a duplicate course check?

    @Query("SELECT COUNT(*) from courses")
    int count();

    @Query("SELECT MAX(courseId) from courses")
    int maxId();

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replaces instance of first course if two courses have same IDs
    Long insert(CourseRoom course); //returns generated id

    @Delete
    void delete(CourseRoom course);

    @Query("DELETE FROM courses")
    void deleteAll();

    @Query("DELETE FROM courses WHERE userId!=:userId")
    void deleteOthers(String userId);

    @Query("SELECT * FROM courses WHERE userId=:userId AND courseName LIKE '%' || :quarterYear")
    List<CourseRoom> getCurrentCourses(String userId, String quarterYear);
}