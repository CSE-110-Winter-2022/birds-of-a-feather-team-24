package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

//        Spinner mySpinner = (Spinner) findViewById(R.id.year_spinner);
//
//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddClassesActivity.this,
//                android.R.layout)

        Spinner yearSpinner = (Spinner) findViewById(R.id.year_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(adapter);


        Spinner quarterSpinner = (Spinner) findViewById(R.id.quarter_spinner);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.quarters, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        quarterSpinner.setAdapter(adapter2);

    }
}