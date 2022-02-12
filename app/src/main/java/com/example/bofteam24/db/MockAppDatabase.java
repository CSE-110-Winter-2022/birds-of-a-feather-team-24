package com.example.bofteam24.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, CourseRoom.class}, version = 1)
public abstract class MockAppDatabase extends RoomDatabase {
    private static MockAppDatabase singletonInstance;

    public static MockAppDatabase singleton(Context context) {
        singletonInstance = Room.databaseBuilder(context, MockAppDatabase.class, "user.db")
                .allowMainThreadQueries()
                .build();

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