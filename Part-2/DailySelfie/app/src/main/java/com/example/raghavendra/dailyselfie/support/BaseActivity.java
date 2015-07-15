package com.example.raghavendra.dailyselfie.support;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.raghavendra.dailyselfie.R;

public class BaseActivity extends ActionBarActivity
{
    private static final String FRAGMENT_CONTAINER_TAG = "FRAGMENT_CONTAINER";

    private Fragment mContentFragment;
    private Toolbar mToolbar;

    public void setContentFragment(Fragment fragment)
    {
        mContentFragment = (Fragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_CONTAINER_TAG);

        if (mContentFragment == null)
        {
            mContentFragment = fragment;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_container, mContentFragment, FRAGMENT_CONTAINER_TAG)
                    .commit();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the actionbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }
}
