package com.example.saurabh.tap_it;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


public class NFCActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback {

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        //mEditText = (EditText) findViewById(R.id.edit_text_field);

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Sorry this device does not have NFC.", Toast.LENGTH_LONG).show();
           // mEditText.setText("Sorry this device does not have NFC.");
            return;
        }
        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    /**
     * Ndef Record that will be sent over via NFC
     * @param nfcEvent
     * @return
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {



        SharedPreferences sharedPreferences=getSharedPreferences("com.example.saurabh.tap_it", Context.MODE_APPEND);
        String message = sharedPreferences.getString("token", "");

        /*
        Log.i("Token in NFC is", message);
        mEditText.setText(message); */
        //String message = mEditText.getText().toString();

        NdefRecord ndefRecord = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        }
        NdefMessage ndefMessage = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ndefMessage = new NdefMessage(ndefRecord);
        }
        return ndefMessage;
    }
}
