package com.example.bofteam24;

import android.content.Context;
import android.widget.Spinner;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.User;
import com.example.bofteam24.sorting.ClassSizeSort;
import com.example.bofteam24.sorting.RecentCommonalitySort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUsers {

    public static void byStrategy(Context context, Spinner sortSpinner, List<User> users) {
        Comparator<User> strategy = null;
        switch(sortSpinner.getSelectedItemPosition()) {
            case 0:
                strategy = Comparator.comparing(User::getNumOfSameCourses).reversed();
                break;
            case 1:
                strategy = new RecentCommonalitySort(UserSelf.getInstance(context), AppDatabase.singleton(context));
                break;
            case 2:
                strategy = new ClassSizeSort(UserSelf.getInstance(context), AppDatabase.singleton(context));
                break;
        }

        Collections.sort(users, strategy);
    }
}
