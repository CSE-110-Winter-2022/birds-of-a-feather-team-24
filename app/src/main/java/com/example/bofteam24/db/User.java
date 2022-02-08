package com.example.bofteam24.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

//    public User(int userId, String name) {
//        this.userId = userId;
//        this.name = name;
//    }

    @PrimaryKey
    public int userId;

    @ColumnInfo
    public String name;
}
