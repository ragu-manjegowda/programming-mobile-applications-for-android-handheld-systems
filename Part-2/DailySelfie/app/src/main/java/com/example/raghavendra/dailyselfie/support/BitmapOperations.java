package com.example.raghavendra.dailyselfie.support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapOperations
{
    public static Bitmap decodeBitmapFile(String filePath)
    {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bitmapOptions);

        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bitmapOptions);

        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float angle)
    {
        if (bitmap != null)
        {
            Matrix matrix = new Matrix();

            matrix.postRotate(angle);

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return null;
    }
}
