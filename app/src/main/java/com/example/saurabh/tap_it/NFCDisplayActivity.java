package com.example.saurabh.tap_it;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NFCDisplayActivity extends Activity {

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_display);
        //mTextView = (TextView) findViewById(R.id.text_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            //mTextView.setText(new String(message.getRecords()[0].getPayload()));
            String nfcConn= String.valueOf(message.getRecords()[0].getPayload());

            Log.i("nfcConn received from one device",nfcConn);

            SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("nfcConnection", nfcConn).apply();

            Toast.makeText(NFCDisplayActivity.this, "Connection Successful", Toast.LENGTH_LONG).show();

            //Make a POST request
            try {
                connectionRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent call=new Intent(getApplicationContext(),ConnectionActivity.class);
            startActivity(call);

        } else
            mTextView.setText("Waiting for NDEF Message");

    }

    public void connectionRequest() throws JSONException {
        Log.i("Connection Request", "made");

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url = "http://52.36.159.253/api/v0.1/relationship/add";

        //Actual Call for login
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);//.getJSONObject("response");

                            String connectionResponse = jsonResponse.getString("response");
                            String connectionStatus = jsonResponse.getString("status");

                            Toast.makeText(NFCDisplayActivity.this, connectionResponse, Toast.LENGTH_SHORT).show();

                            if(connectionStatus.equals("OK")){

                            Intent call = new Intent(getApplicationContext(), ConnectionActivity.class);
                            startActivity(call);
                            finish();

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
                SharedPreferences sharedPreferences=getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);
                String token = sharedPreferences.getString("token", "");

                String nfcConnection = sharedPreferences.getString("nfcConnection", "");

                params.put("token", token);
                params.put("connection_token", nfcConnection);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);


    }
}
