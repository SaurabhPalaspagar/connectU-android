package com.example.saurabh.tap_it;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.example.saurabh.tap_it.R.id.usernameNav;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);

        try {
            String userName = sharedPreferences.getString("username", "");
            String userEmail = sharedPreferences.getString("email", "");

            Log.i("Login Username in Profile activity", userName);


            TextView username  = (TextView) findViewById(R.id.profileUserName);
            username.setText(userName);

            TextView email  = (TextView) findViewById(R.id.profileEmail);
            username.setText(userEmail);
        }
        catch(NullPointerException e){ Log.i("Error is username variable Check ConnectionActivity",e.toString());}

    }

}
