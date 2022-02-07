package com.example.bofteam24.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, CourseRoom.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "user.db")
//                    .createFromAsset("starter-persons.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return singletonInstance;
    }

    public abstract  UserWithCoursesDao userWithCoursesDao();
    public abstract  CourseDao courseDao();
}
