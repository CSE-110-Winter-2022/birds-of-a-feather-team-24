package com.example.bofteam24.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT MAX(userId) from users")
    int maxId();

    @Query("SELECT * FROM users")
    List<User> retrieveAllUsers();

    @Query("SELECT COUNT(*) from users")
    int count();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);
}
