package com.example.bofteam24.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    public User(int userId, String name, String photo_url) {
        this.userId = userId;
        this.name = name;
        this.photo_url = photo_url;
    }

    @PrimaryKey
    public int userId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String photo_url;
}
