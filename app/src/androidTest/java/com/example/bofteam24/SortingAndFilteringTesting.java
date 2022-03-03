package com.example.bofteam24;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bofteam24.Sorting.RecentCommonalitySort;
import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseDao;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.MockAppDatabase;
import com.example.bofteam24.db.User;
import com.example.bofteam24.db.UserDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SortingAndFilteringTesting {
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
    public void testCalcAgeWithinFourQuarters() {
        User mockUser = new User();
        RecentCommonalitySort sorter = new RecentCommonalitySort(mockUser, db);

        assertEquals(-1, sorter.calculateAge("WINTER", 2022));
        assertEquals(0, sorter.calculateAge("FALL", 2021));
        assertEquals(1, sorter.calculateAge("SUMMER SESSION 1", 2021));
        assertEquals(1, sorter.calculateAge("SUMMER SESSION 2", 2021));
        assertEquals(1, sorter.calculateAge("SPECIAL SUMMER SESSION", 2021));
        assertEquals(2, sorter.calculateAge("SPRING", 2021));
        assertEquals(3, sorter.calculateAge("WINTER", 2021));
        assertEquals(4, sorter.calculateAge("FALL", 2020));
    }

    @Test
    public void testCalcAgeBeyondFourQuarters() {
        User mockUser = new User();
        RecentCommonalitySort sorter = new RecentCommonalitySort(mockUser, db);

        assertTrue(4 < sorter.calculateAge("SPECIAL SUMMER SESSION", 2020));
        assertTrue(4 < sorter.calculateAge("FALL", 2018));
        assertTrue(4 < sorter.calculateAge("WINTER", 2019));
    }

    @Test
    public void testCalcScoreOnCourseWithAge4() {
        User mockUser = new User();
        RecentCommonalitySort sorter = new RecentCommonalitySort(mockUser, db);

        CourseRoom course = new CourseRoom(0, mockUser.getUserId(),
                "CSE 12 FALL 2020");

        assertEquals(1, sorter.calculateCourseScore(course));
    }

    @Test
    public void testCalcScoreOnCourseWithAgeGreaterThan4() {
        User mockUser = new User();
        RecentCommonalitySort sorter = new RecentCommonalitySort(mockUser, db);

        CourseRoom course = new CourseRoom(0, mockUser.getUserId(),
                "CSE 12 FALL 2018");

        assertEquals(1, sorter.calculateCourseScore(course));
    }

    @Test
    public void testCalcScoreOnCourseWithAge0() {
        User mockUser = new User();
        RecentCommonalitySort sorter = new RecentCommonalitySort(mockUser, db);

        CourseRoom course = new CourseRoom(0, mockUser.getUserId(),
                "CSE 12 FALL 2021");

        assertEquals(5, sorter.calculateCourseScore(course));
    }

    @Test
    public void testCalcScoreOnCourseWithAgeBetween0And4() {
        User mockUser = new User();
        RecentCommonalitySort sorter = new RecentCommonalitySort(mockUser, db);

        CourseRoom course = new CourseRoom(0, mockUser.getUserId(),
                "CSE 12 SUMMER SESSION 1 2021");

        assertEquals(4, sorter.calculateCourseScore(course));
    }

    @Test
    public void testCalcScoreOnCourseWithCurrentQuarter() {
        User mockUser = new User();
        RecentCommonalitySort sorter = new RecentCommonalitySort(mockUser, db);

        CourseRoom course = new CourseRoom(0, mockUser.getUserId(),
                "CSE 12 WINTER 2022");

        assertEquals(0, sorter.calculateCourseScore(course));
    }
}
