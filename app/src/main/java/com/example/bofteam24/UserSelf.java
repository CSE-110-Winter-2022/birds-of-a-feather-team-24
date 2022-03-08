package com.example.bofteam24;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.User;

import static android.content.Context.MODE_PRIVATE;

public final class UserSelf {
    private static User user;

    private UserSelf(){}

    public static User getInstance(Context context) {
        if(user == null) {
            SharedPreferences pref = context.getSharedPreferences("USER_SHARED_PREF", MODE_PRIVATE);
            String storedID = pref.getString(context.getString(R.string.user_id_key), "");
            if(!storedID.equals("")) {
                AppDatabase db = AppDatabase.singleton(context);
                user = db.userDao().getUserWithId(storedID);
                if (user != null) {
                    return user;
                }
            }
            user = new User();
        }

        return user;
    }

    public static void storeID(Context context) {
        SharedPreferences pref = context.getSharedPreferences("USER_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(context.getString(R.string.user_id_key), user.getUserId());
        editor.apply();
    }
}
