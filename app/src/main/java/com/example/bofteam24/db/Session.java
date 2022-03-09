package com.example.bofteam24.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class Session {

    public Session(int entryNum, @NonNull String sessionName, String userId) {
        this.entryNum = entryNum;
        this.sessionName = sessionName;
        this.userId = userId;
    }

    @PrimaryKey(autoGenerate = true) // auto generate a unique entry number for each entry
    private int entryNum = 0;

    @NonNull
    private String sessionName;

    @ColumnInfo
    private String userId;
}
