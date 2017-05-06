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
import android.widget.EditText;
import android.widget.Spinner;

import com.ayranisthenewraki.heredot.herdot.model.CulturalHeritageObject;
import com.ayranisthenewraki.heredot.herdot.model.TimeLocationCouple;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class addChoActivity extends AppCompatActivity {

    static final int ADD_TIME_LOCATION_REQUEST = 1;

    static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    List<TimeLocationCouple> tlcList = new ArrayList<TimeLocationCouple>();

    String title = "";
    String description = "";
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cho);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Spinner subjectDropdown = (Spinner)findViewById(R.id.subjectSpinner);
        String[] items = new String[]{"Select a subject category", "Painting", "Sculpture", "Book/Manuscript", "Handcraft", "Archaeological Artifact",
            "Archaeological Site", "Architecture", "Museum", "Natural Site", "Underwater Site", "Oral Tradition",
                "Performing Art", "Ritual/Tradition", "Festival/Event"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        subjectDropdown.setAdapter(adapter);

        final Button addTimeAndLocation = (Button) findViewById(R.id.addTimeAndLocation);

        addTimeAndLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                openMapsView(view);

            }
        });

        final EditText titleField = (EditText)findViewById(R.id.title);
        final EditText descriptionField = (EditText)findViewById(R.id.description);

        final Button addChoButton = (Button) findViewById(R.id.addChoButton);

        addChoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                title = titleField.getText().toString();
                description = descriptionField.getText().toString();
                category = subjectDropdown.getSelectedItem().toString();

                if(title.length() == 0 || description.length() == 0 || category.equals("Select a subject category") || tlcList.size()==0){

                    String message = "Please fill all fields to continue";

                    if(title.length()>0 && description.length()>0){
                        if(tlcList.size() == 0){
                            message = "Please add time location information by clicking the Add Time Location button";
                        }
                        if(category.equals("Select a subject category")){
                            message = "Please select a category from the dropdown menu";
                        }
                    }

                    android.app.AlertDialog alertDialog = new AlertDialog.Builder(addChoActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage(message);
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    addChoObject(view);
                }


            }
        });

    }

    public void openMapsView(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("userToken", intent.getStringExtra("userToken"));
        startActivityForResult(intent, ADD_TIME_LOCATION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_TIME_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();

                TimeLocationCouple tlc = (TimeLocationCouple)bundle.getSerializable("timeLocation");

                tlc.getShape().setId(tlcList.size());

                if (tlcList.size() > 25) {
                    tlc.getShape().setIdentifier("A_" + tlcList.size());
                }
                tlc.getShape().setIdentifier(Character.toString(alphabet[tlcList.size()]));

                tlcList.add(tlc);

                // Do something with the contact here (bigger example below)
            }
        }
    }

    private void addChoObject(View view){

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        List<String> stringTlcList = new ArrayList<String>();
        for(TimeLocationCouple tlc : tlcList){
            String tlcString = tlc.toString();
            stringTlcList.add(tlcString);
        }

        CulturalHeritageObject cho = new CulturalHeritageObject();
        cho.setCategory(category);
        cho.setDescription(description);
        cho.setTitle(title);
        cho.setTimeLocations(stringTlcList);

        String json = gson.toJson(cho);
        json = Normalizer.normalize(json, Normalizer.Form.NFD);

        System.out.print(json);
    }

}
