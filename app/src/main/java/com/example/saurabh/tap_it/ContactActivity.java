package com.example.saurabh.tap_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    TextView contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);}
        catch (Exception e){
            e.printStackTrace();
        }

        populateContact(); //twitterUrl,linkedInUrl,resumeUrl,domainUrl

        SharedPreferences sharedPreferences=getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);
        final String linkedInUrl = sharedPreferences.getString("linkedInUrl", "");
        final String twitterUrl = sharedPreferences.getString("twitterUrl", "");
        final String resumeUrl = sharedPreferences.getString("resumeUrl", "");
        final String domainUrl = sharedPreferences.getString("domainUrl", "");
        Log.i("resume link is first",resumeUrl);

        ImageView linkedInImg = (ImageView)findViewById(R.id.linkedInImageView);
        linkedInImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(linkedInUrl.matches("(?i)http.*")){
                Uri uri = Uri.parse(linkedInUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);}
                else{
                    Toast.makeText(ContactActivity.this, "No URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView twitterImg = (ImageView)findViewById(R.id.twitterImageView);
        twitterImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(twitterUrl.matches("(?i)http.*")){
                Uri uri = Uri.parse(twitterUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);}
                else{
                    Toast.makeText(ContactActivity.this, "No URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView resumeImg = (ImageView)findViewById(R.id.resumeImageView);
        resumeImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(resumeUrl.matches("(?i)http.*")){
                Uri uri = Uri.parse(resumeUrl);
                Log.i("resume link is",resumeUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);}
            else{
                Toast.makeText(ContactActivity.this, "No URL", Toast.LENGTH_SHORT).show();
            }
            }
        });
        ImageView domainImg = (ImageView)findViewById(R.id.domailImageView);
        domainImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(domainUrl.matches("(?i)http.*")){
                Uri uri = Uri.parse(domainUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);}
        else{
            Toast.makeText(ContactActivity.this, "No URL", Toast.LENGTH_SHORT).show();
        }
            }
        });

    }

    public void populateContact(){
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences=getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);
        String token = sharedPreferences.getString("token", "");

        String url ="http://52.36.159.253/api/v0.1/relationship/list?token="+token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            SharedPreferences sharedPreferences=getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);
                            String token = sharedPreferences.getString("token", "");
                            int position = sharedPreferences.getInt("userLink",0);

                            JSONObject responseArray=new JSONObject(response);
                            JSONArray jsonResponse = responseArray.getJSONArray("response");
                            String name,company;
                            String twitterUrl,linkedInUrl,resumeUrl,domainUrl;

                            for(int i=0;i<jsonResponse.length();i++)
                                if (i == position) {
                                    JSONObject connectionObject = jsonResponse.getJSONObject(i);
                                    name = connectionObject.getString("name");
                                    twitterUrl=connectionObject.getString("twitter");
                                    linkedInUrl=connectionObject.getString("linkedin");
                                    resumeUrl=connectionObject.getString("resume");
                                    domainUrl=connectionObject.getString("website");
                                    Log.i("resume link stored is",resumeUrl);

                                    sharedPreferences.edit().putString("twitterUrl", twitterUrl).apply();
                                    sharedPreferences.edit().putString("linkedInUrl", linkedInUrl).apply();
                                    sharedPreferences.edit().putString("resumeUrl", resumeUrl).apply();
                                    sharedPreferences.edit().putString("domainUrl", domainUrl).apply();
                                    try{
                                    contactName = (TextView) findViewById(R.id.contactName);
                                    contactName.setText(name);}
                                    catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
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
    public void onResume(){
        super.onResume();
        populateContact();
    }
}