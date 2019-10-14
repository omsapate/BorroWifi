package com.sumitkolhe.borrowifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
///////////////////////////////////////////////////////////////////////////////

    //Declarations for objects

    String macaddressforjson;
    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private TextView mactextview;
    private Button nextbutton;
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////

    //ONCREATE FUNCTION

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ///////////////////////////////////////
        ////INITIALIZING ALL THE VIEWS
        buttonScan = findViewById(R.id.scanBtn);
        mactextview = findViewById(R.id.mac);
        nextbutton = findViewById(R.id.nextBtn);
        listView = findViewById(R.id.wifiList);
        listView.setSelector(R.color.transparent);
        ////////////////////////////////////////

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scanWifi();
                openwifisettings();
            }
        });

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openpricing();

            }
        });

        //CHECKING IF WIFI SERVICE IS ON OR OFF
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {

            wifiManager.setWifiEnabled(true);
        }

        //SETTING ADAPTER FOR SCANNED WIFI LIST
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        scanWifi();
        macaddressforjson = getMacAddr();
        mactextview.setText(getMacAddr());



    }

    //END OF ONCREATE

/////////////////////////////////////////////////////////////////////////////////////////

//FUNCTION TO RETRIEVE MAC ADDRESS
public static String getMacAddr() {
    try {
        List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
        for (NetworkInterface nif : all) {
            if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

            byte[] macBytes = nif.getHardwareAddress();
            if (macBytes == null) {
                return "";
            }

            StringBuilder res1 = new StringBuilder();
            for (byte b : macBytes) {
                res1.append(String.format("%02X:",b));
            }

            if (res1.length() > 0) {
                res1.deleteCharAt(res1.length() - 1);
            }
            return res1.toString();
        }
    } catch (Exception ex) {
    }
    return "02:00:00:00:00:00";
}
////////////////////////////////////////////////////////////////////////////////////////

//BROADCAST RECEIVER TO SCAN FOR AVAILABLE WIFI HOTSPOTS
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                if (scanResult.SSID.contains("borrowifi")) {
                    arrayList.add(scanResult.SSID );
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

///////////////////////////////////////////////////////////////////

//FUNCTIONS
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    private void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

//////////////////////////////////////////////////////////////////////////////////////

//INTENTS TO LAUNCH OTHER ACTIVITES

    public void openpricing(){
        Intent intent = new Intent(this, Pricing.class);
        startActivity(intent);
    }
    private void openwifisettings() {
        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
    }
//////////////////////////////////////////////////////////////////////////////////////

}



