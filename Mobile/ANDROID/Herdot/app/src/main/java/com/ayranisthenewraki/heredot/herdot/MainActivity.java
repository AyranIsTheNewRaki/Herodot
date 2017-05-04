package com.ayranisthenewraki.heredot.herdot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.ayranisthenewraki.heredot.herdot.model.User;
import com.ayranisthenewraki.heredot.herdot.util.NetworkManager;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
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
import retrofit2.http.Path;


public class MainActivity extends AppCompatActivity {

    final String APIURL = "http://api.herodot.world";

    User user;

    String username;
    String password;
    String email;

    boolean registered = false;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final EditText emailInput;
        final EditText usernameInput;
        final EditText passwordInput;
        final TextView outputField;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emailInput = (EditText) findViewById(R.id.emailField);
        usernameInput = (EditText) findViewById(R.id.usernameField);
        passwordInput = (EditText) findViewById(R.id.passwordField);
        outputField = (TextView) findViewById(R.id.emailAndPassword);

        final Button submitButton = (Button) findViewById(R.id.signInButton);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                email = emailInput.getText().toString();
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();

                // make call to backend to verify user
                if(submitButton.getText().equals("Login")){

                    if(!username.equals("idilgun") || !password.equals("123456")){
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("The username or password is incorrect");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else{

                        //openHeritageItemsPage(view);
                    }

                }
                // make call to backend to create new user
                else{

                    if (!NetworkManager.isNetworkAvailable(MainActivity.this)) {
                        RelativeLayout coordinatorLayout = (RelativeLayout)findViewById(R.id.content_main);
                        Snackbar.make(coordinatorLayout,"Check internet connection",Toast.LENGTH_LONG).show();
                    }
                    if (progress == null) {
                        progress = new ProgressDialog(MainActivity.this);
                        progress.setTitle("User Registration");
                        progress.setMessage("Please wait...");
                    }
                    progress.setCancelable(false);
                    progress.show();
                    register(view);
                    //openHeritageItemsPage(view);
                }
            }
        });

        final Button showLoginButton = (Button) findViewById(R.id.showLoginFields);
        showLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(showLoginButton.getText().equals("I Already Have an Account")){
                    emailInput.setVisibility(View.INVISIBLE);
                    usernameInput.setHint("Enter Username or Password");
                    submitButton.setText("Login");
                    showLoginButton.setText("I Want to Create an Account");
                }else{
                    emailInput.setVisibility(View.VISIBLE);
                    usernameInput.setHint("Enter Username");
                    submitButton.setText("SignIn");
                    showLoginButton.setText("I Already Have an Account");
                }

            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void openHeritageItemsPage(View view) {
        Intent intent = new Intent(this, HeritageItemHomepageActivity.class);
        startActivity(intent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void register(final View view){
        final ObjectNode userJson = makeUserJson();
        OkHttpClient client = NetworkManager.getNewClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();
        RegisterService service = retrofit.create(RegisterService.class);
        final Call<String> reservationCall = service.completeRegistration(userJson);

        //make an asynchronous call.
        reservationCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.dismiss();
                if (response.code() == 201) {

                    openHeritageItemsPage(view);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

    private ObjectNode makeUserJson(){
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode userJson = factory.objectNode();
        userJson.put("email", email);
        userJson.put("password", password);
        userJson.put("username", username);
        return userJson;
    }

    private interface RegisterService {

        @Headers({"Content-Type: application/json"})
        @POST("/register")
        Call<String> completeRegistration(@Body ObjectNode user);
    }
}
