package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Theerawuth on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String CRIME_POSITION = "CrimeFragment.CRIME_POS";
    private static final String DIALOG_DATE = "CrimeFragment.CRIME.DIALOG";
    private static final int REQUEST_DATE = 12345;
    private static final int REQUEST_TIME = 12;
    private static final int REQUEST_CONTACT_SUSPECT = 121;
    private static final String DIALOG_TIME = "CrimeFragment.CRIME.TIME";
    private CrimeLab crimeLab;

    private Crime crime;
    private EditText editText;
    private Button crimeDateButton;
    private Button crimeTimeButton;
    private CheckBox crimeSolvedCheckbox;
    private Button crimeDeleteButton;
    private Button crimeReportButton;
    private Button crimeSuspectButton;
    private Button crimeCallButton;


    public CrimeFragment() {

    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);


        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
//        if(getArguments().get(CRIME_ID) != null){
        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        crime = crimeLab.getCrimeById(crimeId);
//        }
//        else
//        {
//            Crime crime = new Crime();
//            crimeLab.addCrime(crime);
//            this.crime = crime;
//        }

        Log.d(CrimeListFragment.TAG, "crime.getId()=" + crime.getId());
        Log.d(CrimeListFragment.TAG, "crime.getTitle()=" + crime.getTitle());

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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        crimeDateButton = (Button) v.findViewById(R.id.crime_date);
        crimeDateButton.setText(crime.getCrimeDate().toString());
        crimeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // เรียกใช้ Dialog DATE
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialogFragment = DatePickerFragment.newInstance(crime.getCrimeDate());

                dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialogFragment.show(fm, DIALOG_DATE);
            }

        });

        crimeSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSolvedCheckbox.setChecked(crime.getSolved());
        crimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);

                Log.d(CrimeListFragment.TAG, "Crime:" + crime.toString());
            }
        });

        crimeTimeButton = (Button) v.findViewById(R.id.crime_time);
        crimeTimeButton.setText(getFormattedTime(crime.getCrimeDate()));
        crimeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerFragment timeDialogFragment = TimePickerFragment.newInstance(crime.getCrimeDate());
                timeDialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timeDialogFragment.show(fm, DIALOG_TIME);
            }
        });

        crimeDeleteButton = (Button) v.findViewById(R.id.delete_button);
        crimeDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeLab.getInstance(getActivity()).deleteCrime(crime.getId());
//                List<Crime> crimeList = crimeLab.getCrime();
//                for(int i =0; i < crimeList.size(); i++)
//                {
//                    if(crime.getId() == crimeList.get(i).getId())
//                    {
//                        crimeLab.getCrime().remove(i);
//                    }
//                }
                getActivity().finish();

            }
        });

        crimeReportButton = (Button) v.findViewById(R.id.crime_report);
        crimeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); // MIME TYPE
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));

                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);


        crimeSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        crimeSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT_SUSPECT);
            }
        });

        if(crime.getSuspect() != null){
            crimeSuspectButton.setText(crime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            crimeSuspectButton.setEnabled(false);
        }




        crimeCallButton = (Button) v.findViewById(R.id.crime_call_suspect);
        crimeCallButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String phone = crime.getSuspect();
                        callIntent.setData(Uri.parse(phone));
                startActivity(callIntent);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int result, Intent data) {
        if (result != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            //set
            crime.setCrimeDate(date);
            crimeDateButton.setText(getFormattedDate(crime.getCrimeDate()));
        }

        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            //set
            crime.setCrimeDate(date);
            crimeTimeButton.setText(getFormattedTime(crime.getCrimeDate()));
        }

        if(requestCode == REQUEST_CONTACT_SUSPECT){
            if(data != null){
                Uri contactUri = data.getData();
                String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME };

                Cursor c = getActivity()
                        .getContentResolver()
                        .query(contactUri,
                                queryFields,
                                null,
                                null,
                                null
                        );
                try {
                    if (c.getCount() == 0){
                        return;
                    }

                    c.moveToFirst();
                    String suspect = c.getString(
                            c.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    crime.setSuspect(suspect);
                    crimeSuspectButton.setText(suspect);
                    }
                    finally
                    {
                        c.close();
                    }
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getActivity()).updateCrime(crime); // update crime in db
    }

    private String getCrimeReport() {
        String solvedString = null;

        if (crime.getSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM  dd";
        String dateString = DateFormat.format(dateFormat, crime.getCrimeDate()).toString();

        String suspect = crime.getSuspect();

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_with_suspect);
        }

        String report = getString(R.string.crime_report,
                crime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    private String getFormattedDate(Date crimeDate) {
        return new SimpleDateFormat("dd MMMM yyyy").format(crimeDate);
    }

    private String getFormattedTime(Date crimeDate) {
        return new SimpleDateFormat("hh:mm a").format(crimeDate);
    }
}