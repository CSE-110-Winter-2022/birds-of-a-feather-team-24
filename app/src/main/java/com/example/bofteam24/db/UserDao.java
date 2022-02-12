package com.example.bofteam24.db;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT MAX(userId) from users")
    int maxId();


}
