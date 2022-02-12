package com.example.bofteam24.db;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, CourseRoom.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "user.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return singletonInstance;
    }

    public void closeDatabase() {
        if (singletonInstance.isOpen()) {
            singletonInstance.getOpenHelper().close();
        }
    }

    public abstract  UserWithCoursesDao userWithCoursesDao();
    public abstract  CourseDao courseDao();
    public abstract UserDao userDao();
}
