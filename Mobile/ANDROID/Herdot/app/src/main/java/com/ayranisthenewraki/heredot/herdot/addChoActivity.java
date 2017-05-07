package com.ayranisthenewraki.heredot.herdot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ayranisthenewraki.heredot.herdot.model.CulturalHeritageObject;
import com.ayranisthenewraki.heredot.herdot.model.TimeLocationCouple;
import com.ayranisthenewraki.heredot.herdot.model.TimeLocationListWrapper;
import com.ayranisthenewraki.heredot.herdot.util.NetworkManager;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class addChoActivity extends AppCompatActivity {

    ProgressDialog progress;

    final String APIURL = "http://api.herodot.world";
    static final int ADD_TIME_LOCATION_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int VIEW_TIME_LOCATION_REQUEST = 3;

    static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    List<TimeLocationCouple> tlcList = new ArrayList<TimeLocationCouple>();

    String title = "";
    String description = "";
    String category = "";

    String userToken ="";

    Bitmap imageBitmap;

    File photoFile = null;


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

        final Button viewTimeLocation = (Button) findViewById(R.id.viewTimeLocation);


        viewTimeLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                openMapsViewAllActivity(view);

            }
        });

        final Button cameraButton = (Button) findViewById(R.id.cameraButton);

        cameraButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                dispatchTakePictureIntent();

            }
        });

        final EditText titleField = (EditText)findViewById(R.id.title);
        final EditText descriptionField = (EditText)findViewById(R.id.description);

        final Button addChoButton = (Button) findViewById(R.id.addChoButton);

        addChoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                userToken = getIntent().getStringExtra("userToken");

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
                    if (!NetworkManager.isNetworkAvailable(addChoActivity.this)) {
                        RelativeLayout coordinatorLayout = (RelativeLayout)findViewById(R.id.content_add_cho);
                        Snackbar.make(coordinatorLayout,"Check internet connection", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if (progress == null) {
                            progress = new ProgressDialog(addChoActivity.this);
                            progress.setTitle("Adding Cultural Heritage Object");
                            progress.setMessage("Please wait...");
                        }

                        progress.setCancelable(false);
                        progress.show();
                        addChoObject(view);
                    }
                }


            }
        });

    }

    public void openMapsView(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, ADD_TIME_LOCATION_REQUEST);
    }

    public void openMapsViewAllActivity(View view){
        Intent intent = new Intent(this, MapsViewAllActivity.class);
        Bundle bundle = new Bundle();
        TimeLocationListWrapper tlcListWrapper = new TimeLocationListWrapper();
        tlcListWrapper.setTlcList(tlcList);
        bundle.putSerializable("timeLocationList", tlcListWrapper);
        intent.putExtras(bundle);
        startActivityForResult(intent, VIEW_TIME_LOCATION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_TIME_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Button viewTimeLocation = (Button) findViewById(R.id.viewTimeLocation);
                if(viewTimeLocation.getVisibility()==View.INVISIBLE){
                    viewTimeLocation.setVisibility(View.VISIBLE);
                }

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

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.imageView2);
            mImageView.setImageBitmap(imageBitmap);

        }
    }

    private void addChoObject(final View view){

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

        if(imageBitmap!=null){
            String url = sendImageToServer();
            if(url!=null){
                cho.setImageUrl(url);
            }

        }

        String json = gson.toJson(cho);
        json = Normalizer.normalize(json, Normalizer.Form.NFD);

        OkHttpClient client = NetworkManager.getNewClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        AddChoService addChoService = retrofit.create(AddChoService.class);
        final Call<String> addChoCall =  addChoService.addCHO(json, userToken);

        addChoCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.dismiss();
                if (response.code() == 201) {

                    openHeritageItemsPage(view);

                } else {
                    String message = "Something went wrong, please try again";

                    AlertDialog alertDialog = new AlertDialog.Builder(addChoActivity.this).create();
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

                AlertDialog alertDialog = new AlertDialog.Builder(addChoActivity.this).create();
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

    private interface AddChoService {

        @Headers({"Content-Type: application/json"})
        @POST("/heritage")
        Call<String> addCHO(@Body String json, @Header("Authorization") String authKey);
    }

    public void openHeritageItemsPage(View view) {
        Intent intent = new Intent(this, HeritageItemHomepageActivity.class);
        intent.putExtra("userToken", userToken);
        startActivity(intent);
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "culturalHeritageImage";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                this.getCacheDir()      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }



    private String sendImageToServer(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try{
            File file = createImageFile();
            FileOutputStream fOut = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

            Map config = new HashMap();
            config.put("cloud_name", "dxkj4gojg");
            config.put("api_key", "684449743758261");
            config.put("api_secret", "8VI0mZY_ezLg1wwdOJU589d5Rb0");
            Cloudinary cloudinary = new Cloudinary(config);

            Map uploadResult = cloudinary.uploader().upload(file.getAbsolutePath(), Cloudinary.asMap());

            Object url = uploadResult.get("url");
            return url.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
