package com.augmentis.ayp.crimin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by Theerawuth on 8/4/2016.
 */
public class DialogPicture extends DialogFragment {

    ImageView dialogPicture;
    String pathPicture;


    public static DialogPicture newInstance(String path) {
        DialogPicture dp = new DialogPicture();
        Bundle args = new Bundle();
        args.putSerializable("ARG_PATH", path);
        dp.setArguments(args);
        return dp;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        pathPicture = (String) getArguments().getSerializable("ARG_PATH");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogpicture, null);
        dialogPicture = (ImageView) v.findViewById(R.id.crime_dialog_photo);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);

        Bitmap bitmap = PictureUtile.getScaledBitmap(pathPicture, getActivity());
        dialogPicture.setImageBitmap(bitmap);


        return builder.create();

    }
}

