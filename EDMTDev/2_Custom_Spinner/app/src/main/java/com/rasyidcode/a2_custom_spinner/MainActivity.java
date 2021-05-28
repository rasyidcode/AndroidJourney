package com.rasyidcode.a2_custom_spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> listSource = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateData();

        Spinner spinnerTest = findViewById(R.id.spinner_test);
        spinnerTest.setAdapter(new SpinnerAdapter(listSource, MainActivity.this));
        spinnerTest.setDropDownVerticalOffset(100);
    }

    private void generateData() {
        for (int i = 0; i < 10; i++) {
            listSource.add("Item " + i);
        }
    }
}