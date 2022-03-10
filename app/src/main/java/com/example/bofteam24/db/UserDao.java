

package com.example.bofteam24.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT MAX(userId) from users")
    int maxId();

    @Transaction
    @Query("SELECT * FROM users")
    List<User> retrieveAllUsers();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM users WHERE userId=:userId")
    User getUserWithId(String userId);

    @Query("SELECT COUNT(*) from users")
    int count();

    @Transaction
    @Query("SELECT * FROM users WHERE userId!=:userId")
    List<User> getOthers(String userId);

    @Query("DELETE FROM users WHERE userId!=:userId")
    void deleteOthers(String userId);
}
