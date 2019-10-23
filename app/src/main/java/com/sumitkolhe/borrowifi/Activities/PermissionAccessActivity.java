package com.sumitkolhe.borrowifi.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sumitkolhe.borrowifi.R;

public class PermissionAccessActivity extends AppCompatActivity {

    Button enablebtn;
    Context context;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super .onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_permissionaccess) ;
        enablebtn = findViewById(R.id.enablelocationbtn);


        checkpermissions();
        locationEnabled();

        //intent to open location settings
        enablebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        locationenabledbyuser();
                    }
                }, 500);

            }
        });

    }

    //to check permissions and ask from user to allow them
    private void checkpermissions() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        showdenieddialog();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { token.continuePermissionRequest();}
                }).check();
    }

    //checks if location is already enabled and if it is enabled then it skips the whole permission activity.
    private void locationEnabled () {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isEnabled) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

    }

    //changes the enable location button to next after enabling locattion from the settings.
    void locationenabledbyuser(){

        enablebtn.setText("Next");
        enablebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                perform();
            }
        });
    }

    //intent to open home activity
    public void perform() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    //dialog to ask user again for location permissions if the user denies it the first time.
    public void showdenieddialog(){
        new MaterialStyledDialog.Builder(this)
                .setTitle("Allow Location Access")
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription("BorroWifi needs location access to scan nearby WIFI connections")
                .setHeaderColor(R.color.location_dialog_header)
                .withDivider(true)
                .setPositiveText("Allow")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkpermissions();
                    }})
                .setNegativeText("EXIT")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                    }
                })
                .show();
    }
}