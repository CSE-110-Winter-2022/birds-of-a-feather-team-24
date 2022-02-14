package com.example.bofteam24;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseDao;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;
import com.example.bofteam24.db.UserDao;

import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTesting {
    private UserDao userDao;
    private CourseDao courseDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
//        AppDatabase.useTestSingleton(context.getApplicationContext());
//        db =
        userDao = db.userDao();
        courseDao = db.courseDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsertUser() {
        User user1 = new User("0", "Kirtan", "kirtan_profile.url", 0);
        User user2 = new User("1", "Brett", "brett_profile.url", 0);

        userDao.insert(user1);
        userDao.insert(user2);

        assertEquals(2, userDao.count());
    }

    @Test
    public void testDeleteUser() {
        User user1 = new User("0", "Kirtan", "kirtan_profile.url", 0);
        User user2 = new User("1", "Brett", "brett_profile.url", 0);

        userDao.insert(user1);
        userDao.insert(user2);
        userDao.delete(user2);

        assertEquals(1, userDao.count());
    }

    @Test
    public void testInsertCourse() {
        CourseRoom course1 = new CourseRoom(0, "0", "CSE 11 FALL 2020");
        courseDao.insert(course1);
        assertEquals(0, courseDao.maxId());

        CourseRoom course2 = new CourseRoom(courseDao.maxId()+1, "0", "CSE 12 WINTER 2021");
        courseDao.insert(course2);
        assertEquals(1, courseDao.maxId());

        CourseRoom course3 = new CourseRoom(courseDao.maxId()+1, "1", "CSE 11 FALL 2020");
        courseDao.insert(course3);
        assertEquals(2, courseDao.maxId());

        CourseRoom course4 = new CourseRoom(courseDao.maxId()+1, "1", "CSE 12 WINTER 2021");
        courseDao.insert(course4);
        assertEquals(3, courseDao.maxId());

        assertEquals(4, courseDao.count());
        assertEquals(3, courseDao.maxId());
    }

    @Test
    public void testDeleteCourse() {
        CourseRoom course1 = new CourseRoom(0, "0", "2020 FALL CSE 11");
        CourseRoom course2 = new CourseRoom(1, "0", "2021 WINTER CSE 12");
        CourseRoom course3 = new CourseRoom(2, "1", "2020 FALL CSE 11");
        CourseRoom course4 = new CourseRoom(3, "1", "2021 WINTER CSE 12");

        courseDao.insert(course1);
        courseDao.insert(course2);
        courseDao.insert(course3);
        courseDao.insert(course4);
        courseDao.delete(course3);
        courseDao.delete(course2);

        assertEquals(2, courseDao.count());
    }
}