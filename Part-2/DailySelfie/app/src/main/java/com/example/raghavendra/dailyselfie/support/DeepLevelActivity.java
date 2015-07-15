package com.example.raghavendra.dailyselfie.support;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;

public class DeepLevelActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            ActivityCompat.finishAfterTransition(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
