package com.sumitkolhe.borrowifi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.sumitkolhe.borrowifi.R;

public class ConnectedActivity extends AppCompatActivity {

    LottieAnimationView connectedanimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        connectedanimation = findViewById(R.id.connected_animation);
        connectedanimation.setRepeatCount(0);
        connectedanimation.setSpeed(0.5f);

    }

    public void home(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
