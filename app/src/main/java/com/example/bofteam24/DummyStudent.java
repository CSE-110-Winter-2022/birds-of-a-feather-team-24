package com.example.bofteam24;

import java.util.ArrayList;

public class DummyStudent {
    private int id;
    private String firstName;
    private String lastName;
    private ArrayList<String> courses;

    public DummyStudent(int id, String firstName, String lastName, ArrayList<String> courses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
    }

    public int getId() { return this.id; }

    public String getName() { return this.firstName + " " + this.lastName; }

    public ArrayList<String> getCourses() {
        return this.courses;
    }
}
