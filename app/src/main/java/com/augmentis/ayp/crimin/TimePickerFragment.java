package com.augmentis.ayp.crimin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import java.sql.Time;

/**
 * Created by Theerawuth on 7/28/2016.
 */
public class TimePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {


    //Step 1: build TimePicker
    public static TimePickerFragment newInstance(Time time){
        TimePickerFragment tf = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME", time);
        tf.setArguments(args);
        return tf;
    }

    //Step 3:



}
