package com.example.bofteam24;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public final class ParseUtils {
    private ParseUtils() { }

    public static boolean wave;

    /*
    Method to clean up the incoming cvs input. Remove spaces, new lines, etc.
    This way it is easier to obtain the data that is needed.
     */
    public static String[] cleanCVSInput(String csvInfo) {

        String[] csvInfoDivided = csvInfo.split(",");
        String[] dividedByNewLine = csvInfo.split("\n");
        String csvInfoDividedSize = String.valueOf(csvInfoDivided.length);
        String dividedByNewLineSize = String.valueOf(dividedByNewLine.length);

//        Log.d("---------------- size of csvInfoDivided", csvInfoDividedSize);
//        Log.d("---------------- size of dividedByNewLine", dividedByNewLineSize);
        if (dividedByNewLine.length == 5) {
            ParseUtils.wave = false;
        }
        else if (dividedByNewLine.length > 5) {
            ParseUtils.wave = true;
        }
        for(int i = 0; i < csvInfoDivided.length; i++) {
            String index = String.valueOf(i);
            if (i >=3 ) {
                csvInfoDivided[i] = csvInfoDivided[i].trim();
                csvInfoDivided[i] = csvInfoDivided[i].replace("\n", "_"); //--> added the "_"
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
    public static void printAllCourses(ArrayList<String> allCoursesInfo) {
        for(String cInfo : allCoursesInfo) {
            Log.d("------------Course Info", cInfo);
        }
    }

    public static String getUserId(String[] csvInfoDivided) {
        return csvInfoDivided[0];
    }

    public static String getUserFirstName(String[] csvInfoDivided) {
        return csvInfoDivided[4];
    }

    public static String getUserPhotoURL(String[] csvInfoDivided) {
        return csvInfoDivided[8];
    }

    /*
    Method to get the first name and the url from the cvs input.
     */
    public static String[] getFirstNameAndUrl(String[] csvInfoDivided) {
        String userId = csvInfoDivided[0]; // added
        String firstName = csvInfoDivided[4];
        String photoURL = csvInfoDivided[8];
//        for(int i = 0; i < csvInfoDivided.length; i++) {
//            if (i == csvInfoDivided.length - 1) {
//                break;
//            }
//            if (i == 0) {
//                userId = csvInfoDivided[i];
//                Log.d("---------------- userId", userId);
//            }
//            if (i == 4) { // prev (i == 0)
//                firstName = csvInfoDivided[i];
//                Log.d("---------------- firstName", firstName);
//            } else if (i == 8) { // prev (i == 3)
//                photoURL = csvInfoDivided[i];
//                Log.d("---------------- photoURL", photoURL);
//            }
//        }

        return new String[]{firstName, photoURL};
    }

    /*
    Method to get all the courses of a user from the cvs input.
     */
    public static ArrayList<String> getAllCoursesInfo(String[] csvInfoDivided) {
        if (ParseUtils.wave) {
            Log.d("------------- ", "THERE IS A WAVE IN CSV INPUT");
            return allCoursesWave(csvInfoDivided);
        }

        Log.d("------------- ", "THERE IS NO WAVE IN CSV INPUT");
        return allCoursesNoWave(csvInfoDivided);
    }

    private static ArrayList<String> allCoursesWave(String[] csvInfoDivided) {
        ArrayList<String> allCoursesInfo = new ArrayList<String>();
        String courseInfo = "";
        String testYear = "";
        for(int i = 0; i < csvInfoDivided.length; i++) {
            // Log.d("----------------------- " + i + ") csvInfoDivided[i] ", csvInfoDivided[i]);

            if (csvInfoDivided[i].length() == 36 && csvInfoDivided[i].contains("-")) { // curr string is UUID
                continue;
            }

            if (csvInfoDivided[i].equals("wave")) {
                String userIdOfWavie = csvInfoDivided[i-1];
                if (userIdOfWavie.length() > 36) {
                    userIdOfWavie = userIdOfWavie.split("_")[1];
                }
                Log.d("------------------------------- UserId ", userIdOfWavie + " WAVING!!!!!!");
                allCoursesInfo.add(userIdOfWavie + "_waves");
            }

            if (i == csvInfoDivided.length-1) {break;}
            else if (i == 12) { // prev (i == 6)
                String courseYear = csvInfoDivided[i];
                String quarter = csvInfoDivided[i+1];
                String subject = csvInfoDivided[i+2];
                String courseNumber = csvInfoDivided[i+3]; // prev String courseNumber = csvInfoDivided[i+3].split("-")[0];
                String courseSize = csvInfoDivided[i+4].split("_")[0]; // added

                courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 3;
            }
            else if (i >= 12 && !csvInfoDivided[i].equals("wave") && !csvInfoDivided[i].equals("") &&
                    !csvInfoDivided[i].equals(" ")) { // prev (i >= 6)

                testYear = csvInfoDivided[i].split("_")[1];

                if (testYear.length() == 4) {
                    String courseYear = csvInfoDivided[i].split("_")[1];
                    String quarter = csvInfoDivided[i+1];
                    String subject = csvInfoDivided[i+2];
                    String courseNumber = csvInfoDivided[i+3];
                    String courseSize = csvInfoDivided[i+4].split("_")[0];

                    courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
                    allCoursesInfo.add(courseInfo);
                    i = i + 3;
                }

            }
        }

        return allCoursesInfo;
    }

    private static ArrayList<String> allCoursesNoWave(String[] csvInfoDivided) {
        ArrayList<String> allCoursesInfo = new ArrayList<String>();
        String courseInfo = "";

        for(int i = 0; i < csvInfoDivided.length; i++) {
            // Log.d("----------------------- " + i + ") csvInfoDivided[i] ", csvInfoDivided[i]);
            if (i == csvInfoDivided.length-1) {break;}
            else if (i == 12) { // prev (i == 6)
                String courseYear = csvInfoDivided[i];
                String quarter = csvInfoDivided[i+1];
                String subject = csvInfoDivided[i+2];
                String courseNumber = csvInfoDivided[i+3]; // prev String courseNumber = csvInfoDivided[i+3].split("-")[0];
                String courseSize = csvInfoDivided[i+4].split("_")[0]; // added
                courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 3;
            }
            else if (i >= 12) { // prev (i >= 6)

                String courseYear = csvInfoDivided[i].split("_")[1];
                String quarter = csvInfoDivided[i+1];
                String subject = csvInfoDivided[i+2];
                String courseNumber = csvInfoDivided[i+3];
                String courseSize = csvInfoDivided[i+4].split("_")[0];

                courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 3;
            }
        }

        return allCoursesInfo;
    }
}
