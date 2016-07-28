package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Theerawuth on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String CRIME_POSITION = "CrimeFragment.CRIME_POS";
    private static final String DIALOG_DATE = "CrimeFragment.CRIME.DIALOG";
    private static final int REQUEST_DATE = 12345;
    private Crime crime;
    private int position;
    private EditText editText;
    private Button crimeDateButton;
    private CheckBox crimeSolvedCheckbox;

    public CrimeFragment()
    {

    }

    public static CrimeFragment newInstance(UUID crimeId, int position){
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID,crimeId);
        args.putInt(CRIME_POSITION,position);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);

        position = getArguments().getInt(CRIME_POSITION);
        crime = CrimeLab.getInstance(getActivity()).getCrimeById(crimeId);
        Log.d(CrimeListFragment.TAG,"crime.getId()=" +crime.getId());
        Log.d(CrimeListFragment.TAG,"crime.getTitle()=" +crime.getTitle());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_criminal, container, false);

        editText = (EditText) v.findViewById(R.id.crime_title);
        editText.setText(crime.getTitle());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
                addThisPositionToResult(position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        crimeDateButton = (Button) v.findViewById(R.id.crime_date);
        crimeDateButton.setText(crime.getCrimeDate().toString());
        crimeDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                // เรียกใช้ Dialog
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialogFragment = DatePickerFragment.newInstance(crime.getCrimeDate());

                dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialogFragment.show(fm, DIALOG_DATE);

            }

        });

        crimeSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSolvedCheckbox.setChecked(crime.getSolved());
        crimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                Log.d(CrimeListFragment.TAG,"Crime:" + crime.toString());
            }
        });

        Intent intent = new Intent();
        intent.putExtra("position", position);
        getActivity().setResult(Activity.RESULT_OK, intent);

        return v;
    }

    private void addThisPositionToResult(int position){
        if(getActivity() instanceof CrimePagerActivity){
            ((CrimePagerActivity) getActivity()).addPageUpdate(position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int result, Intent data) {
        if(result != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            //set
            crime.setCrimeDate(date);
            crimeDateButton.setText(getFormattedDate(crime.getCrimeDate()));
            addThisPositionToResult(position);
        }
    }

    private String getFormattedDate(Date crimeDate) {
        return new SimpleDateFormat("dd MMMM yyyy").format(crimeDate);
    }
}