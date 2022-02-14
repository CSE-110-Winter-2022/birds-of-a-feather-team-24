package com.example.bofteam24;

import android.util.Log;

import java.util.ArrayList;

public final class ParseUtils {
    private ParseUtils() { }

    /*
    Method to clean up the incoming cvs input. Remove spaces, new lines, etc.
    This way it is easier to obtain the data that is needed.
     */
    public static String[] cleanCVSInput(String csvInfo) {

        String[] csvInfoDivided = csvInfo.split(",");
        String arrSize = String.valueOf(csvInfoDivided.length);

//        Log.d("---------------- size of  csvInfoDivided", arrSize);
        for(int i = 0; i < csvInfoDivided.length; i++) {
            String index = String.valueOf(i);
            if (i >=3 ) {
                csvInfoDivided[i] = csvInfoDivided[i].trim();
                csvInfoDivided[i] = csvInfoDivided[i].replace("\n", "-"); //--> added the "-"
            }
//            Log.d("---------------- index", index);
//            Log.d("--- csvInfoDivided[i]", csvInfoDivided[i]);
//            Log.d("--- size of ^", String.valueOf(csvInfoDivided[i].length()));
        }

        return csvInfoDivided;
    }

    /*
    Prints all the courses of a student in this format: 2021 FA CSE 110.
    Used only for debugging.
     */
    private void printAllCourses(ArrayList<String> allCoursesInfo) {
        for(String cInfo : allCoursesInfo) {
            Log.d("------------Course Info", cInfo);
        }
    }

    /*
    Method to get the first name and the url from the cvs input.
     */
    public static String[] getFirstNameAndUrl(String[] csvInfoDivided) {
        String firstName = "";
        String photoURL = "";
        for(int i = 0; i < csvInfoDivided.length; i++) {
            if (i == csvInfoDivided.length - 1) {
                break;
            }
            if (i == 0) {
                firstName = csvInfoDivided[i];
            } else if (i == 3) {
                photoURL = csvInfoDivided[i];
            }
        }

        return new String[]{firstName, photoURL};
    }

    /*
    Method to get all the courses of a user from the cvs input.
     */
    public static ArrayList<String> getAllCoursesInfo(String[] csvInfoDivided) {
        ArrayList<String> allCoursesInfo = new ArrayList<String>();
        String courseInfo = "";

        for(int i = 0; i < csvInfoDivided.length; i++) {
            if (i == csvInfoDivided.length-1) {break;}
            else if (i == 6) {
                String courseNumber = csvInfoDivided[i+3].split("-")[0];
                String courseYear = csvInfoDivided[i];
                courseInfo = courseYear + " " + csvInfoDivided[i + 1] + " " + csvInfoDivided[i + 2]
                        + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 2;
            }
            else if (i >= 6) {
                String courseNumber = csvInfoDivided[i+3].split("-")[0];
                String courseYear = csvInfoDivided[i].split("-")[1];

                courseInfo = courseYear + " " + csvInfoDivided[i + 1] + " " + csvInfoDivided[i + 2]
                        + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 2;
            }
        }

        return allCoursesInfo;
    }
}
