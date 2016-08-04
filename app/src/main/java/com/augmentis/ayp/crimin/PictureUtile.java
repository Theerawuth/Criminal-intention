package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Theerawuth on 8/4/2016.
 */
public class PictureUtile {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // Read the dimansion of the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // return null and put mata data (information about the bitmap)
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;

        if(srcHeight > destHeight || srcWidth > destWidth){
            if(srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight/destWidth);
            }
            else
            {
                inSampleSize = Math.round(srcWidth/destWidth);
            }

        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;



        return BitmapFactory.decodeFile(path, options);

    }


    public static Bitmap  getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }
}
