package com.rasyidcode.a6_advanced_search_view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search Bar");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        MaterialSearchView materialSearchView = findViewById(R.id.search_view);
    }
}