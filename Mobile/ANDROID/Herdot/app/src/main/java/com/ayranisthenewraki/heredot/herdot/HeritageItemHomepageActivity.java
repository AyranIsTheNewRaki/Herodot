package com.ayranisthenewraki.heredot.herdot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ayranisthenewraki.heredot.herdot.model.CulturalHeritageObject;
import com.ayranisthenewraki.heredot.herdot.model.ExpandableListAdapter;
import com.ayranisthenewraki.heredot.herdot.model.TimeLocationCouple;
import com.ayranisthenewraki.heredot.herdot.model.TimeLocationListWrapper;
import com.ayranisthenewraki.heredot.herdot.util.NetworkManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public class HeritageItemHomepageActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, CulturalHeritageObject> listDataChild;
    Context _context;

    final String APIURL = "http://api.herodot.world";
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heritage_item_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _context = this;

        if (!NetworkManager.isNetworkAvailable(HeritageItemHomepageActivity.this)) {
            RelativeLayout coordinatorLayout = (RelativeLayout)findViewById(R.id.content_main);
            Snackbar.make(coordinatorLayout,"Check internet connection", Toast.LENGTH_LONG).show();
        }
        else{
            if (progress == null) {
                progress = new ProgressDialog(HeritageItemHomepageActivity.this);
                progress.setTitle("Getting Cultural Heritage Items");
                progress.setMessage("Please wait...");
            }

            progress.setCancelable(false);
            progress.show();
            getCHOs(findViewById(R.id.content_heritage_item_homepage));
        }

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

    private void prepareListData(List<CulturalHeritageObject> choList) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, CulturalHeritageObject>();

        for(int i=0; i<choList.size(); i++){
            CulturalHeritageObject cho = choList.get(i);
            listDataHeader.add(cho.getTitle());
            listDataChild.put(cho.getTitle(), cho);
        }
    }

    private void getCHOs(final View view){

        OkHttpClient client = NetworkManager.getNewClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        HeritageItemHomepageActivity.getChoService getChoService = retrofit.create(HeritageItemHomepageActivity.getChoService.class);
        final Call<String> getCHOCall =  getChoService.getCHO(getIntent().getStringExtra("userToken"));

        getCHOCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.dismiss();
                if (response.code() == 200) {

                    Gson gson = new Gson();
                    String responseBody = response.body();
                    Type listType = new TypeToken<ArrayList<CulturalHeritageObject>>(){}.getType();

                    List<CulturalHeritageObject> choList = gson.fromJson(responseBody, listType);

                    for(CulturalHeritageObject cho : choList){
                        List<TimeLocationCouple> tlcList = new ArrayList<TimeLocationCouple>();
                        for(String timeLocation: cho.getTimeLocations()){
                            TimeLocationCouple tlcCouple = gson.fromJson(timeLocation, TimeLocationCouple.class);
                            tlcList.add(tlcCouple);
                        }
                        cho.setActualTimeLocations(tlcList);
                    }

                    expListView = (ExpandableListView) findViewById(R.id.lvExp);

                    // preparing list data
                    prepareListData(choList);

                    listAdapter = new ExpandableListAdapter(_context, listDataHeader, listDataChild);

                    // setting list adapter
                    expListView.setAdapter(listAdapter);

                } else {
                    String message = "Could not get Cultural Heritage Items";

                    AlertDialog alertDialog = new AlertDialog.Builder(HeritageItemHomepageActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage(message);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(HeritageItemHomepageActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Something went wrong, please try again");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    private interface getChoService {

        @Headers({"Content-Type: application/json"})
        @GET("/heritage")
        Call<String> getCHO(@Header("Authorization") String authKey);
    }

}
