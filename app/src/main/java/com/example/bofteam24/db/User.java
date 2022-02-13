package com.example.bofteam24.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "users")
public class User {

//    public User() {
//        this.userId = "";
//        this.name = "";
//        this.photoUrl = "";
//    }

    public User(@NonNull String userId, String name, String photoUrl, int numOfSameCourses) {
        this.userId = userId;
        this.name = name;
        this.photoUrl = photoUrl;
        this.numOfSameCourses = numOfSameCourses;
    }

    @PrimaryKey // userID must be different for every user
    @NonNull
    private String userId;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String photoUrl;

    private int numOfSameCourses;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getNumOfSameCourses() { return numOfSameCourses; }

    public void setNumOfSameCourses(int numOfSameCourses) { this.numOfSameCourses = numOfSameCourses; }
}
