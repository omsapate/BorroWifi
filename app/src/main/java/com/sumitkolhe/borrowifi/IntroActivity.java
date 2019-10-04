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
        shouldDarkenButtonsLayout(true);
////////////////////////////////////////////////

        ////////////////////////////////////////

        OnboarderPage onboarderPage1 = new OnboarderPage("Planet Earth", "Our lovely pale blue dot", R.drawable.img3);
        OnboarderPage onboarderPage2 = new OnboarderPage("Venus", "The love goddess", R.drawable.img1);
        OnboarderPage onboarderPage3 = new OnboarderPage("Mars", "Say hi to Curiosity!", R.drawable.img2);

        onboarderPage1.setBackgroundColor(R.color.colorPrimary);
        onboarderPage2.setBackgroundColor(R.color.colorPrimary);
        onboarderPage3.setBackgroundColor(R.color.colorPrimary);

        List<OnboarderPage> pages = new ArrayList<>();

        pages.add(onboarderPage1);
        pages.add(onboarderPage2);
        pages.add(onboarderPage3);

        for (OnboarderPage page : pages) {
            page.setTitleColor(R.color.colorPrimary);
            page.setDescriptionColor(R.color.colorAccent);
            page.setDescriptionTextSize(20);

        }

        setSkipButtonTitle("Skip");
        setFinishButtonTitle("Finish");

        setOnboardPagesReady(pages);

    }

    @Override
    public void onSkipButtonPressed() {
        super.onSkipButtonPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onFinishButtonPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}