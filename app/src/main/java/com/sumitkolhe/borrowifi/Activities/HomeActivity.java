package com.sumitkolhe.borrowifi.Activities;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.sumitkolhe.borrowifi.R;

import java.util.ArrayList;
import java.util.List;

import static com.sumitkolhe.borrowifi.utils.MacAddressfinder.getMacAddr;


public class HomeActivity extends ListActivity {
///////////////////////////////////////////////////////////////////////////////

    //Declarations for objects

    String macaddressforjson;
    private WifiManager wifiManager;
    WifiScanReceiver wifiReceiver;
    ListView listView;
    //private Button buttonScan;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private TextView mactextview;
    private Button nextbutton;
    String wifiresultsize[];
    LottieAnimationView refreshbutton;
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////

    //ONCREATE FUNCTION
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///////////////////////////////////////
        ////INITIALIZING ALL THE VIEWS
        //buttonScan = findViewById(R.id.scanBtn);
       // mactextview = findViewById(R.id.mac);
        refreshbutton = findViewById(R.id.refresh);
        nextbutton = findViewById(R.id.nextBtn);
        listView=getListView();
        getListView().setDivider(null);
        getListView().setDividerHeight(5);
        wifiReceiver = new WifiScanReceiver();
        macaddressforjson = getMacAddr();
        //mactextview.setText(getMacAddr());
        ////////////////////////////////////////

        //open next screen on pressing next button on main activity
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openpricing();

            }
        });

        //CHECKING IF WIFI SERVICE IS ON OR OFF
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this,"OFF",Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        //making wifi scan reults in list view clickable, so as to allow us to connect to any one of them on selecting.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String ssid = ((TextView) view).getText().toString();
                connectToWifi(ssid);
                Toast.makeText(HomeActivity.this,"Wifi SSID : "+ssid,Toast.LENGTH_SHORT).show();

            }
        });

        //scan wifi connections and populate the list view with it.
        scanWifi();

        //to refresh scanned wifi results on button click
        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbutton.playAnimation();
                scanWifi();

            }
        });
    }



    //END OF ONCREATE

////////////////////////////////////////////////////////////////////////////////////////

    //Broadcast receiver is used to scan for available wifi hotspots and cast them to the list view created earlier
class WifiScanReceiver extends BroadcastReceiver {
    @SuppressLint("UseValueOf")
    public void onReceive(Context c, Intent intent) {
        results = wifiManager.getScanResults();
        wifiresultsize = new String[results.size()];
        for(int i = 0; i < results.size(); i++){
            wifiresultsize[i] = ((results.get(i)).toString());
        }
        String filtered[] = new String[results.size()];
        int counter = 0;
        for (String eachWifi : wifiresultsize) {

            if (eachWifi.contains("borrowifi")) {
                String[] temp = eachWifi.split(",");
                filtered[counter] = temp[0].substring(5).trim(); //+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
                counter++;
            }
        }
        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, R.id.label, filtered)); //casting the scanned wifi connections onto the list view.

    }
}

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


    private void finallyConnect(String networkSSID) {

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + networkSSID + "\"";
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wifiManager.addNetwork(wifiConfig);
        // remember id
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                Toast.makeText(this,"Connecting...",Toast.LENGTH_LONG).show();
                break;
            }
        }

    }

    private void connectToWifi(final String wifiSSID) {

        new MaterialStyledDialog.Builder(this)
                .setTitle("Connect to WIFI")
                .setStyle(Style.HEADER_WITH_ICON)
                .setDescription("Selected network : "+ wifiSSID)
                .setIcon(R.drawable.ic_wifi)
                .setHeaderColor(R.color.dialog_header)
                .withDarkerOverlay(true)
                .withIconAnimation(true)
                .setCancelable(true)
                .withDivider(true)
                .setPositiveText(R.string.connect_wifi_yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finallyConnect(wifiSSID);
                                }})
                .setNegativeText(R.string.connect_wifi_no)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })

                .show();
    }

//////////////////////////////////////////////////////////////////////////////////////

//INTENTS TO LAUNCH OTHER ACTIVITES

    public void openpricing(){
        Intent intent = new Intent(this, PricingActivity.class);
        startActivity(intent);
    }

    public void about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
//////////////////////////////////////////////////////////////////////////////////////

}



