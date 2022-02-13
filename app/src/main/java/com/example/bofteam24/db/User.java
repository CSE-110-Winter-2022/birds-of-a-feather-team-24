package com.example.bofteam24.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

//    public User() {
//        this.userId = "";
//        this.name = "";
//        this.photoUrl = "";
//    }

    public User(String userId, String name, String photoUrl) {
        this.userId = userId;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    @PrimaryKey
    @NonNull
    private String userId;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String photoUrl;

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
}