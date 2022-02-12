package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bofteam24.db.AppDatabase;

public class MockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
    }

    public void onEnterClick(View view) {
        EditText editText = this.findViewById(R.id.edit_text);
        String cvsInfo = editText.getText().toString();
        StudentsListActivity.cameFromMock = true;
        if (!cvsInfo.equals("")) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("csv_info", cvsInfo);
            editor.apply();
        }

        editText.setText("");
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}