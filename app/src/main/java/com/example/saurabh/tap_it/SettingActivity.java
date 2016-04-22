package com.example.saurabh.tap_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    TextView name;
    TextView email;
    TextView twitterHandle;
    TextView lnUsername;
    TextView resumeURL;
    TextView websiteURL;
    TextView companyName;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contextOfApplication = getApplicationContext(); //passing the context to user
        getProfileData(contextOfApplication);
    }

    public void getProfileData(final Context context){

        SharedPreferences sharedPreferences=getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);
        String token = sharedPreferences.getString("token", "");
        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="http://52.36.159.253/api/v0.1/user/get?token="+token;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject responseObject=new JSONObject(response);
                            JSONObject responseObj=responseObject.getJSONObject("response");

                            String userName=responseObj.getString("name");
                            String userEmail=responseObj.getString("email");
                            String twitter=responseObj.getString("twitter");
                            String linkedin=responseObj.getString("linkedin");
                            String resume=responseObj.getString("resume");
                            String website=responseObj.getString("website");
                            String company=responseObj.getString("company");

                             name=(TextView) findViewById(R.id.settingName);
                             email=(TextView) findViewById(R.id.settingEmail);
                             twitterHandle=(TextView) findViewById(R.id.settingHandle);
                             lnUsername=(TextView) findViewById(R.id.settingUsername);
                             resumeURL=(TextView) findViewById(R.id.settingResumeURL);
                             websiteURL=(TextView) findViewById(R.id.settingWebsite);
                             companyName=(TextView) findViewById(R.id.settingCompanyName);

                            name.setText(userName);
                            email.setText(userEmail);
                            twitterHandle.setText(twitter);
                            lnUsername.setText(linkedin);
                            resumeURL.setText(resume);
                            websiteURL.setText(website);
                            companyName.setText(company);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    //PUT request after saving the edited name
    public void editUserProfile(View view) throws JSONException {

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url = "http://52.36.159.253/api/v0.1/user/edit";

        //Actual Call for login
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);//.getJSONObject("response");

                            String status = jsonResponse.getString("status");
                            String responsesOnSuccess = jsonResponse.getString("response");

                            if(status.equals("OK")){

                                Toast.makeText(SettingActivity.this, responsesOnSuccess, Toast.LENGTH_SHORT).show();
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

                name=(TextView) findViewById(R.id.settingName);
                email=(TextView) findViewById(R.id.settingEmail);
                twitterHandle=(TextView) findViewById(R.id.settingHandle);
                lnUsername=(TextView) findViewById(R.id.settingUsername);
                resumeURL=(TextView) findViewById(R.id.settingResumeURL);
                websiteURL=(TextView) findViewById(R.id.settingWebsite);
                companyName=(TextView) findViewById(R.id.settingCompanyName);

                SharedPreferences sharedPreferences=getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);
                String token = sharedPreferences.getString("token", "");

                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("twitter", twitterHandle.getText().toString());
                params.put("linkedin", lnUsername.getText().toString());
                params.put("resume", resumeURL.getText().toString());
                params.put("website", websiteURL.getText().toString());
                params.put("company", companyName.getText().toString());
                params.put("token", token);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);


    }


}
