package com.ayranisthenewraki.heredot.herdot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.ayranisthenewraki.heredot.herdot.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    String APIURL = "api.herodot.world/register";

    User user;

    String username;
    String password;
    String email;

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

                        openHeritageItemsPage(view);
                    }

                }
                // make call to backend to create new user
                else{

                    new RegisterTask().execute();

                    openHeritageItemsPage(view);
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

    private class RegisterTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try{

                URL url = new URL(APIURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("password", password);
                postDataParams.put("email", email);

                byte[] postDataBytes = postDataParams.toString().getBytes("UTF-8");

                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                connection.setDoOutput(true);
                connection.getOutputStream().write(postDataBytes);

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                for (int c; (c = in.read()) >= 0;)
                    System.out.print((char)c);

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(connection != null){
                    connection.disconnect();
                }
                try {
                    if(bufferedReader != null){
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }
    }
}
