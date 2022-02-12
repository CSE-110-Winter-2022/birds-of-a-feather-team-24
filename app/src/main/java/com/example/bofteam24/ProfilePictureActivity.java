package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.User;

public class ProfilePictureActivity extends AppCompatActivity {
    User newUser = new User();
    Button button_submit;
    EditText urlText;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        String name = getIntent().getStringExtra("username");
        newUser.setUserName(name);

        button_submit = (Button) findViewById(R.id.button_submit);
        urlText = (EditText) findViewById(R.id.profile_picture_url);

        button_submit.setEnabled(false);

        urlText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(checkUrl(charSequence.toString())){
                    button_submit.setEnabled(true);
                    //button_submit.setBackgroundTintMode("#31a037");
                    //android:backgroundTint="#31a037"
                }

                else button_submit.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void saveProfileUrl(){
        TextView urlView = findViewById(R.id.profile_picture_url);
        newUser.setPhotoUrl(urlView.toString());

        //save to db
        db = AppDatabase.singleton(this);
        //db.userDao().insert();



        Intent addClassesAct = new Intent(ProfilePictureActivity.this, AddClassesActivity.class);
        startActivity(addClassesAct);
    }

    public void submitClick(View view) {
        saveProfileUrl();
        this.finish();
    }

    public void skipClick(View view) {
        newUser.setPhotoUrl("https://imgur.com/oljiNUB");
        saveProfileUrl();
        this.finish();
    }

    public boolean checkUrl(String url){
        return URLUtil.isValidUrl(url);
    }
}