package com.example.bofteam24.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface SessionDao {
    @Transaction
    // @Query("SELECT userId FROM sessionEntries INNER JOIN WHERE sessionId=:sessionId")
    @Query("SELECT * FROM users " +
            "INNER JOIN sessionEntries " +
            "ON sessionEntries.userId = users.userId " +
            "WHERE sessionEntries.sessionId=:sessionId")
    List<User> getUsersBySessionId(int sessionId);

    @Transaction
    @Query("SELECT * FROM sessions WHERE sessionId=:sessionId")
    Session getSessionById(int sessionId);

    @Transaction
    @Query("SELECT sessionName FROM sessions")
    List<String> getAll();

    @Transaction
    @Query("SELECT sessionId FROM sessions WHERE sessionName=:sessionName")
    List<Integer> getSessionIdForSession(String sessionName);

//    @Transaction
//    @Query("SELECT userId FROM sessionEntries WHERE sessionId=:sessionId")
//    List<Integer> getUserIdsForSession(String sessionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Session session);

    @Delete
    void delete(Session session);
}