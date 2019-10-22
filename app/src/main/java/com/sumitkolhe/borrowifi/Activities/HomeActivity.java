package com.sumitkolhe.borrowifi.Activities;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
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
    private TextView mactextview;
    private Button nextbutton;
    private ArrayAdapter adapter;
    String wifiresultsize[];
    LottieAnimationView refreshbutton;
    private AccessibilityService mContext;
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
        refreshbutton.setScale(4F);
        refreshbutton.setRepeatCount(2);
        nextbutton = findViewById(R.id.nextBtn);
        listView=getListView();
        getListView().setDivider(null);
        getListView().setDividerHeight(4);
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
            Toast.makeText(this,"WiFi is off",Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        //making wifi scan reults in list view clickable, so as to allow us to connect to any one of them on selecting.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String ssid = ((TextView) view).getText().toString();
                connectToWifi(ssid);

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

        //enablenextbutton();
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
                    filtered[counter] = temp[0].substring(5).trim();// +temp[3].substring(6).trim(); //+"\n" + temp[2].substring(12).trim()+"\n" ;//0->SSID, 2->Key Management 3-> Strength
                    counter++;
                }
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.label, filtered);
            listView.setAdapter(adapter); //casting the scanned wifi connections onto the list view.

        }
    }

///////////////////////////////////////////////////////////////////

//FUNCTIONS

    //To prevent from going back to the getstarted activity
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    //to rescan the wifi connections
    private void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

    protected void onPause() {
        unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    //connect to the wifi using the SSID
    private void finallyConnect(String networkSSID) {

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";

        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        int networkId = wifiManager.addNetwork(conf);
        WifiInfo wifi_inf = wifiManager.getConnectionInfo();
        wifiManager.disableNetwork(wifi_inf.getNetworkId());
        wifiManager.enableNetwork(networkId, true);
    }

    //Dialog for connecting to the selected WIFI
    private void connectToWifi(final String wifiSSID) {

        new MaterialStyledDialog.Builder(this)
                .setTitle("Connect to WIFI")
                .setStyle(Style.HEADER_WITH_ICON)
                .setDescription("Selected network : "+ wifiSSID)
                .setIcon(R.drawable.ic_wifi)
                .setHeaderColor(R.color.dialog_header)
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

   /* public boolean isWifiConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null) && (cm.getActiveNetworkInfo() != null) &&
                (cm.getActiveNetworkInfo().getType() == 1);
    }*/

   /* public void enablenextbutton(){
        if(isWifiConnected()){
            nextbutton.setVisibility(View.VISIBLE);
        }
    }*/

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



