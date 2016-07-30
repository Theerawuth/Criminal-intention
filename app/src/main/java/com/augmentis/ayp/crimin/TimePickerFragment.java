package com.augmentis.ayp.crimin;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Theerawuth on 7/28/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    //Step 1: build TimePicker
    public static TimePickerFragment newInstance(Time time){
        TimePickerFragment tf = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME", time);
        tf.setArguments(args);
        return tf;
    }

    //Step 3:

    TimePicker _timePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //step 3

        Time time = (Time) getArguments().getSerializable("ARG_TIME");

        // Use the current time as the default values for the picker
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        _timePicker = (TimePicker) v.findViewById(R.id.time_picker_in_dialog);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(),this, hour, minute, DateFormat.is24HourFormat(getActivity()));

        TextView timeDialog = new TextView(getActivity());
        timeDialog.setText("TimePicker Title");
        timeDialog.setBackgroundColor(Color.parseColor("#EEE8AA"));
        timeDialog.setPadding(5, 3, 5, 3);
        timeDialog.setGravity(Gravity.CENTER_HORIZONTAL);
        tpd.setCustomTitle(timeDialog);

        return tpd;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}

