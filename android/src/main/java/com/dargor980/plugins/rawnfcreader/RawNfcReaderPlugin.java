package com.dargor980.plugins.rawnfcreader;

import android.util.Log;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.content.Intent;
import android.nfc.NfcAdapter;


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
    private Tag lastTag = null;

    @Override 
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);

        Tag tag = intent.getParcelableExtra(android.nfc.NfcAdapter.EXTRA_TAG);
        if(tag != null) {
            this.lastTag = tag;
            JSObject ret = new JSObject();
            try {
                MifareUltralight mifare = MifareUltralight.get(tag);
                mifare.connect();

                StringBuilder hexData = new StringBuilder();
                
                for(int page = 0; page <= 132; page += 4) {
                    byte[] block = mifare.readPages(page);
                    for (byte b : block) {
                        hexData.append(String.format("%02X", b));
                    }
                }

                byte[] uid = tag.getId();
                StringBuilder uidBuilder = new StringBuilder();
                
                for (byte b : uid) {
                    uidBuilder.append(String.format("%02X", b));
                }

                mifare.close();

                ret.put("uid", uidBuilder.toString());
                ret.put("memoryHex", hexData.toString());
                notifyListeners("nfcFullDump", ret);

            } catch(IOException e) {
                Log.e("RawNfcReader", "Error reading NFC Tag", e);
            }
        }
    }


    @PluginMethod 
    public void writeDump(PluginCall call) {
        String hex = call.getString("memoryHex");

        if (hex == null || hex.length() < 8 ) {
            call.reject("Invalid Hex");
            return;
        }

        Tag tag = this.lastTag;

        if (tag == null) {
            call.reject("No Tag detected");
            return;
        }

        try {
            MifareUltralight mifare = MifareUltralight.get(tag);
            mifare.connect();

            for (int page = 4; page <=129; page++ ){
                int offset = page * 4;
                if (offset + 8 > hex.length()) break;

                byte[] bytes = new byte[4];
                for (int i = 0; i < 4; i++) {
                    String byteHex = hex.substring(offset + i * 2, offset + i * 2 + 2);
                    bytes[i] = (byte) Integer.parseInt(byteHex, 16);
                }

                mifare.writePage(page, bytes);
            }

            mifare.close();

            JSObject ret = new JSObject();
            ret.put("status", "success");
            call.resolve(ret);

        } catch(Exception e) {
            call.reject("Error writing: " + e.getMessage());
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
