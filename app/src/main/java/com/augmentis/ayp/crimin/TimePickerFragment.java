package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Theerawuth on 7/28/2016.
 */
public class TimePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {

    protected static final String EXTRA_TIME = "TimePicker:" ;


    //Step 1: build TimePicker
    public static TimePickerFragment newInstance(Date time){
        TimePickerFragment tf = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME", time);
        tf.setArguments(args);
        return tf;
    }

    //Step 3:
    Date time;
    TimePicker _timePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //step 3

        time = (Date) getArguments().getSerializable("ARG_TIME");

        // Use the current time as the default values for the picker
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        _timePicker = (TimePicker) v.findViewById(R.id.time_picker_in_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _timePicker.setHour(hour);
            _timePicker.setMinute(minute);
        }

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());   // object ที่ใช้สร้าง Dialog แล้วกำหนดค่าต่างๆ ต่อ
        builder.setView(v);
        builder.setTitle(R.string.date_picker_title);
        builder.setPositiveButton(android.R.string.ok, this);
        return  builder.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int hour = 0;
        int minute = 0;

        // TimePicker ---> Model
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = _timePicker.getHour();
            minute = _timePicker.getMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        sendResult(Activity.RESULT_OK, calendar.getTime());
    }
    private void sendResult(int resultCode, Date time){
        if(getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}

