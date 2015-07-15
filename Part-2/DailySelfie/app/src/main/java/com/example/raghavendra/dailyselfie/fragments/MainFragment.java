package com.example.raghavendra.dailyselfie.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.raghavendra.dailyselfie.R;
import com.example.raghavendra.dailyselfie.activities.PictureViewActivity;
import com.example.raghavendra.dailyselfie.adapters.PictureAdapter;
import com.example.raghavendra.dailyselfie.data.PictureData;
import com.example.raghavendra.dailyselfie.receivers.AlarmReceiver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFragment extends Fragment implements PictureAdapter.OnItemSelection
{
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private static final int REQUEST_PICTURE_VIEW = 2;
    private static final String PICTURE_TAG = "BIG_PICTURE";
    private static final String RESULT_TAG = "PICTURE_RESULT";

    private RecyclerView mRecyclerView;
    private LayoutManager mLayoutManager;
    private PictureAdapter mAdapter;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    private String mLastPicturePath = null;
    private String mLastPictureTitle = null;


    public static MainFragment newInstance()
    {
        return new MainFragment();
    }


    private void deletePicture(String picturePath)
    {
        File pictureFile = new File(picturePath);

        pictureFile.delete();

        Log.i("DAILY_SELFIE", "File on path " + picturePath + " was successfully removed");
    }

    private void loadPreviousPictures()
    {
        Log.i("DAILY_SELFIE", "Preparing to load previous pictures.");

        // Check if the external storage is available to read.
        String mediaState = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(mediaState) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(mediaState))
        {
            // The external storage is available to read
            //Get the directory
            File pictureDirectory = new File(Environment.getExternalStorageDirectory(), "DailySelfie");

            // Check if the pictures' directory exists
            if (pictureDirectory.exists() && pictureDirectory.isDirectory())
            {
                // Get all pictures on the directory
                File[] pictures = pictureDirectory.listFiles();

                for (int i = 0; i < pictures.length; i ++)
                {
                    // Create a picture and add it into the pictures adapter
                    PictureData picture = new PictureData();
                    picture.setTitle(pictures[i].getName());
                    picture.setPath(pictures[i].getPath());

                    mAdapter.addPicture(picture);
                }

                Log.i("DAILY_SELFIE", pictures.length + " pictures loaded from " + pictureDirectory.getAbsolutePath());
                mAdapter.notifyDataSetChanged();
            }
            else
            {
                Log.i("DAILY_SELFIE", "There are no previous pictures to load.");
            }
        }
        else
        {
            // Can't read from the external storage
            Log.i("DAILY_SELFIE", "External storage couldn't be found");
            Toast.makeText(getActivity(), "External storage couldn't be found", Toast.LENGTH_LONG).show();
        }
    }

    private void takePicture()
    {
        String mediaState = Environment.getExternalStorageState();

        Log.i("DAILY_SELFIE", "Checking external storage...");
        if (Environment.MEDIA_MOUNTED.equals(mediaState))
        {
            // The external storage is available to read and write
            Log.i("DAILY_SELFIE", "External storage found");

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Check that there is a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
            {
                // Create the file where the picture should go
                File pictureFile = null;

                try
                {
                    // Create a unique image name based on the instant when the picture was taken.
                    String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                    File pictureDirectory = new File(Environment.getExternalStorageDirectory(), "DailySelfie");

                    // Check if the directory exists, if not, create it
                    if (! pictureDirectory.exists())
                    {
                        // Create directory
                        pictureDirectory.mkdir();
                    }

                    pictureFile = File.createTempFile(fileName, ".jpg", pictureDirectory);
                }
                catch(IOException ioe)
                {
                    Log.e("DAILY_SELFIE", "Error ocurred while creating the picture file.");
                }

                if (pictureFile != null)
                {
                    mLastPicturePath = pictureFile.getAbsolutePath();
                    mLastPictureTitle = pictureFile.getName();

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));

                    Log.i("DAILY_SELFIE", "Prepare to take picture");
                    startActivityForResult(takePictureIntent, REQUEST_PICTURE_CAPTURE);
                }

            }
            else
            {
                Log.i("DAILY_SELFIE", "There is no camera application installed on the device");
                Toast.makeText(getActivity(), "There is no camera application installed on the device", Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            // Can't write in external storage
            Log.i("DAILY_SELFIE", "External storage couldn't be found");
            Toast.makeText(getActivity(), "External storage couldn't be found", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemSelected(PictureData picture)
    {
        // Start image viewer activity which shows the selfie in fullscreen
        Intent pictureViewerIntent = new Intent(getActivity(), PictureViewActivity.class);
        pictureViewerIntent.putExtra(PICTURE_TAG, picture);

        startActivityForResult(pictureViewerIntent, REQUEST_PICTURE_VIEW);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Enable custom toolbar
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PictureAdapter();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListener(this);

        // Set alarm service and notifications
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        int alarmInterval = 1000 * 30 * 2;

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarmInterval, alarmInterval, mPendingIntent);

        loadPreviousPictures();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Result from camera activity
        if (requestCode == REQUEST_PICTURE_CAPTURE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                // Check if a picture was taken
                // This prevent the case of take a picture but cancel it.
                Log.i("DAILY_SELFIE", "Checking if picture " + mLastPictureTitle + " on path " + mLastPicturePath + " was successfully created...");
                Bitmap testBitmap = BitmapFactory.decodeFile(mLastPicturePath);


                if (mAdapter != null && testBitmap != null)
                {
                    // Insert image in the recycler view

                    PictureData picture = new PictureData();
                    picture.setTitle(mLastPictureTitle);
                    picture.setPath(mLastPicturePath);

                    mAdapter.addPicture(picture);

                    // Update the recycler view
                    mAdapter.notifyDataSetChanged();

                    Log.i("DAILY_SELFIE", "Picture " + picture.getTitle() + " has been taken and stored.");
                }
                else
                {
                    // The camera didn't take a picture, so remove the empty file.
                    Log.i("DAILY_SELFIE", "Picture " + mLastPictureTitle + " wasn't created. Deleting file... ");
                    deletePicture(mLastPicturePath);
                }
            }

            if (resultCode == Activity.RESULT_CANCELED)
            {
                // The camera didn't rake a picture, so remove the empty file.
                Log.i("DAILY_SELFIE", "Picture " + mLastPictureTitle + " wasn't created. Deleting file... ");
                deletePicture(mLastPicturePath);
            }

        }
        else if (requestCode == REQUEST_PICTURE_VIEW && resultCode == Activity.RESULT_OK)
        {
            // Result from picture viewer
            PictureData picture = (PictureData) data.getSerializableExtra(RESULT_TAG);

            Log.i("DAILY_SELFIE", "Remove operation request. Deleting file " + picture.getPath());
            deletePicture(picture.getPath());

            Log.i("DAILY_SELFIE", "Remove operation request. Deleting picture " + picture.getTitle());
            mAdapter.removePicture(picture);
            mAdapter.notifyDataSetChanged();

            Log.i("DAILY_SELFIE", "Remove operation completed. Picture " + picture.getTitle() + " was removed");
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_take_picture:
                takePicture();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
