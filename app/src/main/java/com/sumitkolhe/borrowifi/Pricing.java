package com.sumitkolhe.borrowifi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Pricing extends AppCompatActivity {

    Button SendData;
    Details userdetails;
    DatabaseReference refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);

        SendData = findViewById(R.id.submitjson);
        userdetails = new Details();
        refer = FirebaseDatabase.getInstance().getReference().child("Members");

        SendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String macjson = MainActivity.getMacAddr();

              userdetails.setMAC_address(macjson);
              int time = userdetails.setTimer(600);

              refer.child(userdetails.getMAC_address()).setValue(time);
              Toast.makeText(Pricing.this,"Data Sent to Database",Toast.LENGTH_LONG).show();
            }
        });
    }
}
