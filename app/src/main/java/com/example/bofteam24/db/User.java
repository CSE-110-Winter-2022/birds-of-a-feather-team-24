package com.example.bofteam24.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "users")
public class User {

    public User() {
        this("", "", "", 0);
    }
    public User(@NonNull String userId, String name, String photoUrl, int numOfSameCourses) {
        this.userId = userId;
        this.name = name;
        this.photoUrl = photoUrl;
        this.numOfSameCourses = numOfSameCourses;
        this.fav = false;
        this.wave = false;
    }

    @PrimaryKey // userID must be different for every user
    @NonNull
    private String userId;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String photoUrl;

    @ColumnInfo
    private int numOfSameCourses;

    @ColumnInfo
    private boolean fav;

    @ColumnInfo
    private boolean wave;

    public boolean getWave() { return this.wave; }

    public void setWave(boolean wave) { this.wave = wave; }

    public boolean getFav() { return this.fav; }

    public void setFav(boolean fav) { this.fav = fav; }

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
