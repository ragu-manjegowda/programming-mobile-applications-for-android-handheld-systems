package com.example.raghavendra.dailyselfie.activities;

import android.os.Bundle;

import com.example.raghavendra.dailyselfie.data.PictureData;
import com.example.raghavendra.dailyselfie.fragments.PictureViewerFragment;
import com.example.raghavendra.dailyselfie.support.DeepLevelActivity;

public class PictureViewActivity extends DeepLevelActivity
{
    private static final String PICTURE_TAG = "BIG_PICTURE";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get activity's intent extra
        Bundle extras = getIntent().getExtras();

        PictureData picture = null;
        if (extras != null)
        {
            picture = (PictureData) extras.getSerializable("BIG_PICTURE");

            // Set toolbar title
            getSupportActionBar().setTitle(picture.getTitle());
        }

        // Create Activity content layout
        setContentFragment(PictureViewerFragment.newInstance(picture));
    }
}
