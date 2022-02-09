package com.example.bofteam24;

import java.util.ArrayList;

public class DummyStudent {
    private String firstName;
    private String lastName;
    private ArrayList<Course> courses;

    public DummyStudent(String firstName, String lastName, ArrayList<Course> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public ArrayList<Course> getCourses() {
        return this.courses;
    }
}
