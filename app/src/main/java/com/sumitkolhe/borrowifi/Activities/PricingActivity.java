package com.sumitkolhe.borrowifi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumitkolhe.borrowifi.Items.Details;
import com.sumitkolhe.borrowifi.R;
import com.sumitkolhe.borrowifi.utils.MacAddressfinder;

public class PricingActivity extends AppCompatActivity implements View.OnClickListener{

    Details userdetails;
    DatabaseReference refer;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        CardView card1 = findViewById(R.id.plan1);
        CardView card2 = findViewById(R.id.plan2);
        CardView card3 = findViewById(R.id.plan3);
        sendButton = findViewById(R.id.sendbtn);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);



        userdetails = new Details();
        refer = FirebaseDatabase.getInstance().getReference().child("Members");

    }

    @Override
    public void onClick(View view) {
       int duration;
       int id;
        switch (view.getId()) {

            case R.id.plan1:
                duration = 60;
                id=1;
                Toast.makeText(this, "1 min", Toast.LENGTH_LONG).show();
                finalSubmit(id,duration);
                break;

            case R.id.plan2:
                duration = 300;
                id=2;
                Toast.makeText(this, "5 min", Toast.LENGTH_LONG).show();
                finalSubmit(id,duration);
                break;

            case R.id.plan3:
                duration = 1800;
                id=3;
                Toast.makeText(this, "30 min", Toast.LENGTH_LONG).show();
                finalSubmit(id,duration);
                break;

        }

    }


    public void finalSubmit(final int id, final int duration){

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendJson(id,duration);
                openconnected();
            }
        });
    }

    public void SendJson(int id,int duration){

        String macjson = MacAddressfinder.getMacAddr();

        userdetails.setMAC_address(macjson);
        int time = userdetails.setTimer(duration);
        refer.child(userdetails.getMAC_address()).setValue(time);
        Toast.makeText(PricingActivity.this, "Data Sent to Database", Toast.LENGTH_LONG).show();

        openconnected();
    }

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

