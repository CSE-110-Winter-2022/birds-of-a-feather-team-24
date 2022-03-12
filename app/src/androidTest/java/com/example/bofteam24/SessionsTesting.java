package com.example.bofteam24;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseDao;
import com.example.bofteam24.db.Session;
import com.example.bofteam24.db.SessionEntry;
import com.example.bofteam24.db.UserDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SessionsTesting {
    private UserDao userDao;
    private CourseDao courseDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        courseDao = db.courseDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testAddingSessionToDatabase() {
        Session session1 = new Session(null, "session1");
        Session session2 = new Session(null, "session2");
        Session session3 = new Session(null, "session3");

        db.sessionDao().insert(session1);
        db.sessionDao().insert(session2);
        db.sessionDao().insert(session3);

        assertEquals(3, db.sessionDao().getAll().size());
    }

    @Test
    public void testAddingSessionEntriesToDatabase() {
        Session session1 = new Session(null, "session1");
        Session session2 = new Session(null, "session2");

        Long id1 = db.sessionDao().insert(session1);
        Long id2 = db.sessionDao().insert(session2);

        SessionEntry sessionEntry1 = new SessionEntry(null, id1, "user1");
        SessionEntry sessionEntry2 = new SessionEntry(null, id1, "user2");
        SessionEntry sessionEntry3 = new SessionEntry(null, id2, "user2");

        db.sessionDao().insert(sessionEntry1);
        db.sessionDao().insert(sessionEntry2);
        db.sessionDao().insert(sessionEntry3);

        assertEquals(2, db.sessionDao().getAll().size());
        assertEquals(3, db.sessionDao().getAllSessionEntries().size());
        assertEquals(2, db.sessionDao().getAllSessionEntriesForSessionId(id1).size());
        assertEquals(1, db.sessionDao().getAllSessionEntriesForSessionId(id2).size());
    }
}

