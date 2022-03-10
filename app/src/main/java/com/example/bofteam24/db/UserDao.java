

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

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE userId=:userId")
    User getUserWithId(String userId);

    @Query("SELECT COUNT(*) from users")
    int count();

    @Query("SELECT * FROM users WHERE userId!=:userId")
    List<User> getOthers(String userId);

    @Query("DELETE FROM users WHERE userId!=:userId")
    void deleteOthers(String userId);

    @Query("UPDATE users SET name=:name WHERE userId=:userId")
    void updateUserName(String userId, String name);

    @Query("UPDATE users SET photoUrl=:photoUrl WHERE userId=:userId")
    void updateUserPhoto(String userId, String photoUrl);

    @Query("UPDATE users SET numOfSameCourses=:numOfSameCourses WHERE userId=:userId")
    void updateNumCourses(String userId, int numOfSameCourses);

    @Query("UPDATE users SET fav=:fav WHERE userId=:userId")
    void updateUserFav(String userId, Boolean fav);

    @Query("UPDATE users SET wave=:wave WHERE userId=:userId")
    void updateUserWave(String userId, Boolean wave);

    @Query("SELECT * FROM users WHERE fav=:fav")
    List<User> getFavUsers(Boolean fav);
}
