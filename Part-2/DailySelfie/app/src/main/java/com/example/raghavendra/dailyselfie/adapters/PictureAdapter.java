package com.example.raghavendra.dailyselfie.adapters;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raghavendra.dailyselfie.R;
import com.example.raghavendra.dailyselfie.data.PictureData;

import java.util.ArrayList;
import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder>
{
    private List<PictureData> mData = new ArrayList<PictureData>();
    private OnItemSelection mListener = null;


    public void setListener(OnItemSelection listener)
    {
        mListener = listener;
    }

    public void addPicture(PictureData picture)
    {
        mData.add(picture);
    }

    public void removePicture(PictureData picture)
    {
        for (int i = 0; i < mData.size(); i++)
        {
            if (picture.getTitle().equals(mData.get(i).getTitle()))
            {
                mData.remove(i);
                break;
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int position)
    {
        View rowView = LayoutInflater.from(parentViewGroup.getContext())
                .inflate(R.layout.row_picture_item, parentViewGroup, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        PictureData picture = mData.get(position);

        viewHolder.mItemIcon.setImageBitmap(BitmapFactory.decodeFile(picture.getPath()));
        viewHolder.mItemTitle.setText(picture.getTitle());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mListener != null)
                {
                    mListener.onItemSelected(mData.get(position));
                }
            }
        });

        viewHolder.itemView.setTag(picture);
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }


    public interface OnItemSelection
    {
        public void onItemSelected(PictureData picture);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mItemIcon;
        TextView mItemTitle;

        public ViewHolder(View view)
        {
            super(view);

            mItemIcon = (ImageView) view.findViewById(R.id.picture_item_icon);
            mItemTitle = (TextView) view.findViewById(R.id.picture_item_title);
        }
    }
}
