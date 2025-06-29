package com.dargor980.plugins.rawnfcreader;

import android.util.Log;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.content.Intent;


import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;

import java.io.IOException;
import java.util.Arrays;

@CapacitorPlugin(name = "RawNfcReader")
public class RawNfcReaderPlugin extends Plugin {

    private static final String TAG = "RawNfcReaderPlugin";

    @Override 
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);
        Log.d(TAG, "new intent received");

        Tag tag = intent.getParcelableExtra(android.nfc.NfcAdapter.EXTRA_TAG);
        if(tag != null) {
            JSObject ret = new JSObject();
            try {
                MifareUltralight mifare = MifareUltralight.get(tag);
                mifare.connect();
                byte[] pageData = mifare.readPages(4);
                mifare.close();

                StringBuilder sb = new StringBuilder();
                for(byte b: pageData) {
                    sb.append(String.format("%02X", b));
                }

                ret.put("rawData", sb.toString());
                notifyListeners("nfcRawTag", ret);
            } catch(IOException e) {
                Log.e(TAG, "Error reading tag", e);
            }
        }
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");
        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);
    }
}
