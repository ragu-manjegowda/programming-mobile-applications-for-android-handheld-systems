package com.example.raghavendra.dailyselfie.activities;

import android.os.Bundle;

import com.example.raghavendra.dailyselfie.fragments.MainFragment;
import com.example.raghavendra.dailyselfie.support.TopLevelActivity;


public class MainActivity extends TopLevelActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentFragment(MainFragment.newInstance());
    }
}
