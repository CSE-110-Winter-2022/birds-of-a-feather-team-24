package com.example.bofteam24;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    @Test
    public void testNewCourseInfoCorrect() {
        Course course = new Course(2022, "Winter", "CSE", "110");
        assertEquals("CSE 110 Winter 2022", course.toString());
    }

}