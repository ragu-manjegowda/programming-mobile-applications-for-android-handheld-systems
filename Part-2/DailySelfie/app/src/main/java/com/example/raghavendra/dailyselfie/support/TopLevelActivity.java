package com.example.raghavendra.dailyselfie.support;

import android.os.Bundle;

public class TopLevelActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
