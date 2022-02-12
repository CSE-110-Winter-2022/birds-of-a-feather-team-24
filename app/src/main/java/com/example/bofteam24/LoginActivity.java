package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bofteam24.db.User;

public class LoginActivity extends AppCompatActivity {
    Button button_name;
    EditText editText;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        button_name = (Button) findViewById(R.id.button_confirm);
        editText = (EditText) findViewById(R.id.name);

        if(loadName().equals(""))
            button_name.setEnabled(false);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().equals("")) button_name.setEnabled(false);
                else button_name.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void saveName(){
        TextView nameView = findViewById(R.id.name);
        name = nameView.getText().toString();
    }

    public String loadName() {
        SharedPreferences name = getPreferences(MODE_PRIVATE);

        String savedName = name.getString("name", "");

        TextView nameView = findViewById(R.id.name);

        nameView.setText(savedName);

        return savedName;
    }

    public void ConfirmClick(View view) {
        Intent profilePictureAct = new Intent(LoginActivity.this, ProfilePictureActivity.class);
        profilePictureAct.putExtra("username", name);
        startActivity(profilePictureAct);
        this.finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveName();
    }
}