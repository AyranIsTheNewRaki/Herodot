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
import android.widget.Button;
import android.widget.ExpandableListView;

import com.ayranisthenewraki.heredot.herdot.model.CulturalHeritageObject;
import com.ayranisthenewraki.heredot.herdot.model.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HeritageItemHomepageActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, CulturalHeritageObject> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heritage_item_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        final Button newChoButton = (Button) findViewById(R.id.newChoButton);

        newChoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openAddChoPage(view);
            }
        });

    }

    public void openAddChoPage(View view) {
        Intent intent = new Intent(this, addChoActivity.class);
        String userToken  = getIntent().getStringExtra("userToken");
        intent.putExtra("userToken", userToken);
        startActivity(intent);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, CulturalHeritageObject>();

        // Adding child data
        listDataHeader.add("Topkapı Palace");
        listDataHeader.add("Yerebatan Sarnıcı");
        listDataHeader.add("İstanbul Modern");

        // Adding child data
        CulturalHeritageObject cho = new CulturalHeritageObject();
        cho.setCategory("bla");
        cho.setTitle("aaa");

        listDataChild.put(listDataHeader.get(0), cho); // Header, Child data
        listDataChild.put(listDataHeader.get(1), cho);
        listDataChild.put(listDataHeader.get(2), cho);
    }

}
