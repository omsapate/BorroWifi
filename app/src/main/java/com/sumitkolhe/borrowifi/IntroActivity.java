package com.sumitkolhe.borrowifi;

import android.content.Intent;
import android.os.Bundle;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends OnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouldUseFloatingActionButton(true);
        shouldDarkenButtonsLayout(false);
        setActiveIndicatorColor(R.color.active_indicator_color);


        OnboarderPage onboarderPage1 = new OnboarderPage("Borrow Wifi", "Easy to use WiFi services for everyone", R.drawable.img1);
        OnboarderPage onboarderPage2 = new OnboarderPage("Easy to use", "Connect to any BorroWifi hotspots near you", R.drawable.img2);
        OnboarderPage onboarderPage3 = new OnboarderPage("Seamless Connectivity", "Enjoy fast and seamless connectivity", R.drawable.img3);

        onboarderPage1.setBackgroundColor(R.color.background_color);
        onboarderPage2.setBackgroundColor(R.color.background_color);
        onboarderPage3.setBackgroundColor(R.color.background_color);

        List<OnboarderPage> pages = new ArrayList<>();

        pages.add(onboarderPage1);
        pages.add(onboarderPage2);
        pages.add(onboarderPage3);

        for (OnboarderPage page : pages) {
            page.setTitleColor(R.color.title_color);
            page.setDescriptionColor(R.color.description_color);
            page.setDescriptionTextSize(16);


        }

        setSkipButtonTitle("Skip");
        setFinishButtonTitle("Finish");
        setOnboardPagesReady(pages);

    }

    @Override
    public void onSkipButtonPressed() {
        super.onSkipButtonPressed();
        Intent intent = new Intent(this, getstarted.class);
        startActivity(intent);

    }

    @Override
    public void onFinishButtonPressed() {
        Intent intent = new Intent(this, getstarted.class);
        startActivity(intent);
    }



}