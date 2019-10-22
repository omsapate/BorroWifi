package com.sumitkolhe.borrowifi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumitkolhe.borrowifi.Items.Details;
import com.sumitkolhe.borrowifi.R;
import com.sumitkolhe.borrowifi.utils.MacAddressfinder;

public class PricingActivity extends AppCompatActivity implements View.OnClickListener{

    //Default declaration of objects.
    Details userdetails;
    //firebase database reference
    DatabaseReference refer;
    Button sendButton;
    ImageView planimage1,planimage2,planimage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        CardView card1 = findViewById(R.id.plan1);
        CardView card2 = findViewById(R.id.plan2);
        CardView card3 = findViewById(R.id.plan3);
        planimage1 = findViewById(R.id.plan1image);
        planimage1.setImageResource(R.drawable.ic_round);
        planimage2 = findViewById(R.id.plan2image);
        planimage2.setImageResource(R.drawable.ic_round);
        planimage3 = findViewById(R.id.plan3image);
        planimage3.setImageResource(R.drawable.ic_round);
        sendButton = findViewById(R.id.sendpricingBtn);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);



        //firebase object to store details of the user's MAC address and the time of the plan the chose.
        userdetails = new Details();
        //reference of the firebase database used to push data to the realtime database
        refer = FirebaseDatabase.getInstance().getReference().child("Members");

    }

    @Override
    public void onClick(View view) {
       int duration;
       int id;

       //switch case to select one of the plans provided to the user
        switch (view.getId()) {

            case R.id.plan1:
                duration = 600;
                id=1;
                finalSubmit(id,duration);
                planimage1.setImageResource(R.drawable.ic_check);
                planimage2.setImageResource(R.drawable.ic_round);
                planimage3.setImageResource(R.drawable.ic_round);
                break;

            case R.id.plan2:
                duration = 1800;
                id=2;
                finalSubmit(id,duration);
                planimage1.setImageResource(R.drawable.ic_round);
                planimage2.setImageResource(R.drawable.ic_check);
                planimage3.setImageResource(R.drawable.ic_round);
                break;

            case R.id.plan3:
                duration = 3600;
                id=3;
                finalSubmit(id,duration);
                planimage1.setImageResource(R.drawable.ic_round);
                planimage2.setImageResource(R.drawable.ic_round);
                planimage3.setImageResource(R.drawable.ic_check);
                break;

        }

    }


    //submit function to submit the selected plan details to the firebase database
    public void finalSubmit(final int id, final int duration){

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendJson(id,duration);
                openconnected();
            }
        });
    }

    //it combines all the data such as MAC address and time and stores it in variables to be sent to the database.
    public void SendJson(int id,int duration){

        String macjson = MacAddressfinder.getMacAddr();

        userdetails.setMAC_address(macjson);
        int time = userdetails.setTimer(duration);
        refer.child(userdetails.getMAC_address()).setValue(time);

        openconnected();
    }

    //intent to open the final connected activity.
    public void openconnected(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(PricingActivity.this, ConnectedActivity.class);
                startActivity(i);
            }
        }, 800);
    }



}

