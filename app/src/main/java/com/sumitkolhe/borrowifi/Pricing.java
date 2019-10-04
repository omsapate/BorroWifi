package com.sumitkolhe.borrowifi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Pricing extends AppCompatActivity implements View.OnClickListener{

    Details userdetails;
    DatabaseReference refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        CardView card1 = findViewById(R.id.plan1);
        CardView card2 = findViewById(R.id.plan2);
        CardView card3 = findViewById(R.id.plan3);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);



        userdetails = new Details();
        refer = FirebaseDatabase.getInstance().getReference().child("Members");

    }

    @Override
    public void onClick(View view) {
        int duration = 0;
        switch (view.getId()) {

            case R.id.plan1:
                duration = 60;
                Toast.makeText(this, "1 min", Toast.LENGTH_LONG).show();
                SendJson(duration);
                openconnected();

                break;

            case R.id.plan2:
                duration = 300;
                Toast.makeText(this, "5 min", Toast.LENGTH_LONG).show();
                SendJson(duration);
                openconnected();
                break;

            case R.id.plan3:
                duration = 1800;
                Toast.makeText(this, "30 min", Toast.LENGTH_LONG).show();
                SendJson(duration);
                openconnected();
                break;

        }
    }
    public void SendJson(int duration){

        String macjson = MainActivity.getMacAddr();

        userdetails.setMAC_address(macjson);
        int time = userdetails.setTimer(duration);
        refer.child(userdetails.getMAC_address()).setValue(time);
        Toast.makeText(Pricing.this, "Data Sent to Database", Toast.LENGTH_LONG).show();
    }

    public void openconnected(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(Pricing.this,Connected.class);
                startActivity(i);
            }
        }, 1500);
    }
}

