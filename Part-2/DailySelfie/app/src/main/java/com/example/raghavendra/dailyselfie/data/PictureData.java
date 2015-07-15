package com.example.raghavendra.dailyselfie.data;


import java.io.Serializable;

public class PictureData implements Serializable
{
    private String mPictureTitle;
    private String mPicturePath;

    public String getTitle()
    {
        return mPictureTitle;
    }

    public void setTitle(String title)
    {
        mPictureTitle = title;
    }

    public String getPath()
    {
        return mPicturePath;
    }

    public void setPath(String path)
    {
        mPicturePath = path;
    }
}
