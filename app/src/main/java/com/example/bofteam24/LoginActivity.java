package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    Button button_name;
    EditText editText;

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
        SharedPreferences name = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor saveName = name.edit();
        TextView nameView = findViewById(R.id.name);
        saveName.putString("name", nameView.getText().toString());
        saveName.apply();
    }

    public String loadName() {
        SharedPreferences name = getPreferences(MODE_PRIVATE);

        String savedName = name.getString("name", "");

        TextView nameView = findViewById(R.id.name);

        nameView.setText(savedName);

        return savedName;
    }

    public void ConfirmClick(View view) { this.finish(); }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveName();
    }
}