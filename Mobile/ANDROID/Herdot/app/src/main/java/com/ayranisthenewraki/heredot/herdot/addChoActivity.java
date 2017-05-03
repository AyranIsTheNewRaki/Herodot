package com.ayranisthenewraki.heredot.herdot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class addChoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cho);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner subjectDropdown = (Spinner)findViewById(R.id.subjectSpinner);
        String[] items = new String[]{"Select a subject category", "Painting", "Sculpture", "Book/Manuscript", "Handcraft", "Archaeological Artifact",
            "Archaeological Site", "Architecture", "Museum", "Natural Site", "Underwater Site", "Oral Tradition",
                "Performing Art", "Ritual/Tradition", "Festival/Event"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        subjectDropdown.setAdapter(adapter);

        Spinner timeResDropdown = (Spinner)findViewById(R.id.timeResSpinner);
        String[] timeItems = new String[]{"Century", "Decade", "Year", "Date (yyyy/mm/dd)"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeItems);
        timeResDropdown.setAdapter(timeAdapter);

        final Button button = (Button) findViewById(R.id.addTimeAndLocation);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                openMapsView(view);

            }
        });

    }

    public void openMapsView(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
