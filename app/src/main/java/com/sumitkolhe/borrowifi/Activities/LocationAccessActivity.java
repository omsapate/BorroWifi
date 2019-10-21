package com.sumitkolhe.borrowifi.Activities;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sumitkolhe.borrowifi.R;

public class LocationAccessActivity extends AppCompatActivity {

    Button enablebtn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super .onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_locationaccess) ;

        locationEnabled();

        enablebtn = findViewById(R.id.enablelocationbtn);

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


    private void locationEnabled () {



        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isEnabled) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

    }


    void locationenabledbyuser(){

       enablebtn.setText("Next");
       enablebtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

              perfrom();
           }
       });
    }


    public void perfrom() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}