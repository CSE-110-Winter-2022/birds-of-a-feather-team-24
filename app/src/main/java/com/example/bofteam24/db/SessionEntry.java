package com.example.bofteam24.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessionEntries")
public class SessionEntry {

    public SessionEntry(Integer entryId, Long sessionId, String userId) {
        this.entryId = entryId;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    @PrimaryKey(autoGenerate = true) // auto generate a unique entry number for each entry
    private Integer entryId = 0;

    @ColumnInfo
    private Long sessionId;

    @ColumnInfo
    private String userId;

    @NonNull
    public Long getSessionId() { return sessionId; }

    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }
}


