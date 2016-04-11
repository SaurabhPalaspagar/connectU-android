package com.example.saurabh.tap_it;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void changeToLoginActivity(View view){
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void registerUser(View view) throws JSONException {
        name=(EditText) findViewById(R.id.nameTextRegister);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.passwordText);

        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(strEmail)) {
            email.setError("Enter a email address!");

        } else if (!isEmailValid(strEmail)) {
            email.setError("Enter a valid email");

        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(strPassword) && !isPasswordValid(strPassword)) {
            password.setError("Please enter password with correct length ");

        }
        loginRequest(view);
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }

    //API POST HTTP call for Login
    public void loginRequest(View view) throws JSONException {
        Log.i("Register Request", "made");

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url = "http://52.36.159.253/api/v0.1/user/register";

        //Actual Call for login
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            writeToFile(response);//Write to internal storage

                            JSONObject jsonResponse = new JSONObject(response);//.getJSONObject("response");

                            String loginResponse = jsonResponse.getString("response");

                            Toast.makeText(RegisterActivity.this, loginResponse, Toast.LENGTH_SHORT).show();

                            if(loginResponse=="Login succeeded"){

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                // params.put("email", "jhr10@njit.edu");
                //params.put("password", "testing");
                name=(EditText) findViewById(R.id.nameTextRegister);
                email=(EditText) findViewById(R.id.email);
                password=(EditText) findViewById(R.id.passwordText);

                Log.i("Email is",email.getText().toString());
                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

    private void writeToFile(String data) {
        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("userData.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
