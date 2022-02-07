package com.example.bofteam24.db;


import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserWithCoursesDao {
    @Transaction
    @Query("SELECT * FROM users")
    List<UserWithCourses> getAll();

    @Query("SELECT * FROM users WHERE userId=:userId")
    UserWithCourses get(int userId);

    @Query("SELECT COUNT(*) from users")
    int count();
}
