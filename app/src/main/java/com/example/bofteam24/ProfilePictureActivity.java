package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bofteam24.db.User;

public class ProfilePictureActivity extends AppCompatActivity {
    User newUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void saveProfileUrl(){
        TextView urlView = findViewById(R.id.profile_picture_url);
        newUser.setPhotoUrl(urlView.toString());
    }

    public void submitClick(View view) {
        //save newUser to db
    }

    public void skipClick(View view) {
        newUser.setPhotoUrl("https://imgur.com/oljiNUB");
    }
}