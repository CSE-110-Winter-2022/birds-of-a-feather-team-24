package com.example.bofteam24.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class Session {
    public Session(String sessionName) {
        this.sessionName = sessionName;
    }

    @PrimaryKey(autoGenerate = true) // auto generate a unique entry number for each entry
    private Integer sessionId = 0;

    @NonNull
    @ColumnInfo
    private String sessionName;

    @NonNull
    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(@NonNull String sessionName) {
        this.sessionName = sessionName;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }
}
