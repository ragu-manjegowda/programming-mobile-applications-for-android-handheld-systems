package com.example.raghavendra.dailyselfie.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.raghavendra.dailyselfie.R;
import com.example.raghavendra.dailyselfie.data.PictureData;
import com.example.raghavendra.dailyselfie.support.BitmapOperations;

public class PictureViewerFragment extends Fragment
{
    private static final String PICTURE_TAG = "PICTURE_DATA";
    private static final String RESULT_TAG = "PICTURE_RESULT";

    private PictureData mPicture;


    public static PictureViewerFragment newInstance(PictureData picture)
    {
        PictureViewerFragment fragment = new PictureViewerFragment();

        // Set arguments
        Bundle args = new Bundle();
        args.putSerializable(PICTURE_TAG, picture);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get fragment arguments
        if (getArguments() != null)
        {
            mPicture = (PictureData) getArguments().getSerializable(PICTURE_TAG);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Enable custom menu icons
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_picture_viewer, container, false);

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.picture_container);

        // Set picture in the image view
        if (mPicture != null)
        {
            pictureView.setImageBitmap(BitmapOperations.rotateBitmap(BitmapFactory.decodeFile(mPicture.getPath()), 90));
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        menuInflater.inflate(R.menu.menu_picture_viewer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_delete_picture:
                // TODO show dialog asking if really remove the picture
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setTitle("Removing picture");
                dialogBuilder.setMessage("Do you really want to remove this picture?");

                dialogBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(RESULT_TAG, mPicture);

                        getActivity().setResult(Activity.RESULT_OK, resultIntent);
                        getActivity().finish();
                    }
                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Do nothing, only close the dialog.
                        dialog.dismiss();
                    }
                });


                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
