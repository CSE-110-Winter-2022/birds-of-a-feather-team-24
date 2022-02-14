package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    Button nameButton;
    EditText editText;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameButton = (Button) findViewById(R.id.button_confirm);
        editText = (EditText) findViewById(R.id.name);

//        if(loadName().equals(""))
            nameButton.setEnabled(false);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) nameButton.setEnabled(false);
                else nameButton.setEnabled(true);
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

//    public String loadName() {
//        SharedPreferences name = getPreferences(MODE_PRIVATE);
//
//        String savedName = name.getString("name", "");
//
//        TextView nameView = findViewById(R.id.name);
//
//        nameView.setText(savedName);
//
//        return savedName;
//    }

    public void onConfirmClick(View view) {
        saveName();
        UserSelf.getInstance(this).setName(name);

        Intent profilePictureAct = new Intent(LoginActivity.this, ProfilePictureActivity.class);
        startActivity(profilePictureAct);
        this.finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        saveName();
    }
}