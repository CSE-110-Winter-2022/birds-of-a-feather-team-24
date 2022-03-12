package com.example.bofteam24;

import android.util.Log;

import com.example.bofteam24.db.CourseRoom;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public final class ParseUtils {
    private ParseUtils() { }

    public static boolean wave;
    public static String TAG = "BoF-Team24";

    /*
    Method to clean up the incoming cvs input. Remove spaces, new lines, etc.
    This way it is easier to obtain the data that is needed.
     */
    public static String[] cleanCSVInput(String csvInfo) {

        String[] csvInfoDivided = csvInfo.split(",");
        String[] dividedByNewLine = csvInfo.split("\n");
        String lastLine = dividedByNewLine[dividedByNewLine.length - 1];

        String csvInfoDividedSize = String.valueOf(csvInfoDivided.length);
        String dividedByNewLineSize = String.valueOf(dividedByNewLine.length);

//        Log.d("---------------- size of csvInfoDivided", csvInfoDividedSize);
//        Log.d("---------------- size of dividedByNewLine", dividedByNewLineSize);
        if (lastLine.contains("wave")) {
            ParseUtils.wave = true;
        }
        else {
            ParseUtils.wave = false;
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

        return new String[]{firstName, photoURL};
    }

    /*
    Method to get all the courses of a user from the cvs input.
     */
    public static ArrayList<String> getAllCoursesInfo(String[] csvInfoDivided) {
        if (ParseUtils.wave) {
            // Log.d("------------- ", "THERE IS A WAVE IN CSV INPUT");
            return allCoursesWave(csvInfoDivided);
        }

        // Log.d("------------- ", "THERE IS NO WAVE IN CSV INPUT");
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

                // 2021 FA CSE 110 Small
                courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber + " " + courseSize;
                // courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
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

                    // 2021 FA CSE 110 Small
                    courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber + " " + courseSize;
                    // courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
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

                // 2021 FA CSE 110 Small
                courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber + " " + courseSize;
                // courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 3;
            }
            else if (i >= 12) { // prev (i >= 6)

                String courseYear = csvInfoDivided[i].split("_")[1];
                String quarter = csvInfoDivided[i+1];
                String subject = csvInfoDivided[i+2];
                String courseNumber = csvInfoDivided[i+3];
                String courseSize = csvInfoDivided[i+4].split("_")[0];

                // 2021 FA CSE 110 Small
                courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber + " " + courseSize;
                // courseInfo = courseYear + " " + quarter + " " + subject + " " + courseNumber;
                allCoursesInfo.add(courseInfo);
                i = i + 3;
            }
        }

        return allCoursesInfo;
    }

    public static int getSameNumCourses1(List<CourseRoom> myCourses, List<String> allCoursesString) {
        int sameNumCourses = 0;
        for(CourseRoom course : myCourses) {
            // String myCourse = course.toMockString();
            String myCourseName = course.getCourseName();
            Log.d("------ my Courses", myCourseName);
            for(String otherCourse : allCoursesString) {
                String[] courseSplit = otherCourse.split(" ");
                String otherCourseName = courseSplit[0] + " " + courseSplit[1] + " " + courseSplit[2] + " " + courseSplit[3];

                // Log.i("PAIRS", myCourse + ";" + otherCourse);
                if (myCourseName.equals(otherCourseName)) {
                    sameNumCourses+=1;
                }
            }
        }

        return sameNumCourses;
    }

    public static int getSameNumCourses2(List<CourseRoom> myCourses, List<CourseRoom> allCoursesRoom) {
        int sameNumCourses = 0;
        for(CourseRoom course : myCourses) {
            // String myCourse = course.toMockString();
            String myCourseName = course.getCourseName();
            // myCourse = myCourse + " Small"; // comment later
            // Log.d("------ my Courses", myCourseName);
            for(CourseRoom otherCourseRoom : allCoursesRoom) {
                String otherCourseName = otherCourseRoom.getCourseName();
                // Log.i("PAIRS", myCourse + ";" + otherCourse);
                if (myCourseName.equals(otherCourseName)) {
                    sameNumCourses+=1;
                }
            }
        }

        return sameNumCourses;
    }
}
