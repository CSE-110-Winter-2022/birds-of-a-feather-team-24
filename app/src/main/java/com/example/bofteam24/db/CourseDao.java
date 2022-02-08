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
    List<CourseRoom> getForUser(int userId);

    @Query("SELECT * FROM courses")
    List<CourseRoom> getAll();

    @Query("SELECT * FROM courses where courseId=:courseId")
    CourseRoom get(int courseId);

    //add a duplicate course check?

    @Query("SELECT COUNT(*) from courses")
    int count();

    @Query("SELECT MAX(courseId) from courses")
    int maxId();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseRoom course);

    @Delete
    void delete(CourseRoom course);
}
