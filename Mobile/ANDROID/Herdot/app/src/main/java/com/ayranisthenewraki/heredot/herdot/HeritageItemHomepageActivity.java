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


public class HeritageItemHomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heritage_item_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button newChoButton = (Button) findViewById(R.id.newChoButton);

        String userToken  = getIntent().getStringExtra("userToken");

        newChoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openAddChoPage(view);
            }
        });

    }

    public void openAddChoPage(View view) {
        Intent intent = new Intent(this, addChoActivity.class);
        startActivity(intent);
    }

}
