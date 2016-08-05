package com.augmentis.ayp.crimin;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by Theerawuth on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String DIALOG_DATE = "CrimeFragment.CRIME.DIALOG";
    private static final String DIALOG_PICTURE = "dialogpic" ;
    private static final String DIALOG_TIME = "CrimeFragment.CRIME.TIME";

    private static final int REQUEST_DATE = 12345;
    private static final int REQUEST_TIME = 12;
    private static final int REQUEST_CONTACT_SUSPECT = 121;
    private static final int REQUEST_CAPTURE_PHOTO = 10;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2891;
    private static final String TAG = "CrimeFragment";

    private Crime crime;
    private EditText editText;
    private Button crimeDateButton;
    private Button crimeTimeButton;
    private CheckBox crimeSolvedCheckbox;
    private Button crimeDeleteButton;
    private Button crimeReportButton;
    private Button crimeSuspectButton;
    private Button crimeCallButton;
    private ImageView photoView;
    private ImageButton photoButton;
    private File photoFile;

    private Callbacks callbacks;
    public CrimeFragment()  {

    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    //Call Back
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());

        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        crime = crimeLab.getCrimeById(crimeId);


        Log.d(CrimeListFragment.TAG, "crime.getId()=" + crime.getId());
        Log.d(CrimeListFragment.TAG, "crime.getTitle()=" + crime.getTitle());


        photoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(crime);





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
                updateCrime(); // update to db
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
                updateCrime();

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

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        //pickContact.addCategory(Intent.CATEGORY_HOME);



        crimeSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        crimeSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT_SUSPECT);
            }
        });

        if (crime.getSuspect() != null) {
            crimeSuspectButton.setText(crime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            crimeSuspectButton.setEnabled(false);
        }

        crimeCallButton = (Button) v.findViewById(R.id.crime_call_suspect);
        crimeCallButton.setEnabled(crime.getSuspect() != null);
        crimeCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasCallPermission()){
                    callSuspect();
                }

            }
        });

        photoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        photoView = (ImageView) v.findViewById(R.id.crime_photo);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogPicture dialogPicture = DialogPicture.newInstance(photoFile.getPath());
                dialogPicture.show(fm, DIALOG_PICTURE);
            }
        });



        // Call camera intent
        final Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //check if we can take photo ?
        boolean canTakePhoto = photoFile != null
                && captureImageIntent.resolveActivity(packageManager) != null;

        if (canTakePhoto){
            Uri uri = Uri.fromFile(photoFile);
            Log.d(TAG, "File output at " + photoFile.getAbsolutePath());
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        // on click --> start activity for camera
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImageIntent, REQUEST_CAPTURE_PHOTO);
            }
        });

        // update photo changing
        updatePhotoView();

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
            updateCrime();
            crimeDateButton.setText(getFormattedDate(crime.getCrimeDate()));
        }

        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            //set
            crime.setCrimeDate(date);
            updateCrime();
            crimeTimeButton.setText(getFormattedTime(crime.getCrimeDate()));
        }

        if(requestCode == REQUEST_CONTACT_SUSPECT) {
            if(data != null) {
                Uri contactUri = data.getData();
                String[] queryFields = new String[] {
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER };

                Cursor c = getActivity()
                        .getContentResolver()
                        .query(contactUri,
                                queryFields,
                                null,
                                null,
                                null);

                try {
                    if(c.getCount() == 0) {
                        return ;
                    }

                    c.moveToFirst();
                    String suspect = c.getString(0);
                    suspect = suspect + ":" + c.getString(1);

                    crime.setSuspect(suspect);
                    updateCrime();
                    crimeSuspectButton.setText(suspect);
                    crimeCallButton.setEnabled(suspect != null);
                } finally {
                    c.close();
                }
            }
        }

        //update photo
        if(requestCode == REQUEST_CAPTURE_PHOTO) {
            updatePhotoView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        updateCrime();



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

    private void callSuspect() {
        Intent i = new Intent(Intent.ACTION_CALL);
        StringTokenizer tokenizer = new StringTokenizer(crime.getSuspect(), ":");
        String name = tokenizer.nextToken();
        String phone = tokenizer.nextToken();
        Log.d(TAG, "calling " + name + "/" + phone);
        i.setData(Uri.parse("tel:" + phone));

        startActivity(i);
    }

    private void updateCrime(){
        CrimeLab.getInstance(getActivity()).updateCrime(crime); // update crime in db
        if(this.isResumed()){
            callbacks.onCrimeUpdated(crime);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                //if request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Granted permission
                    callSuspect();

                } else {
                    //Denied permission
                    Toast.makeText(getActivity(),
                            R.string.denied_permission_to_call,
                            Toast.LENGTH_LONG)
                            .show();
                }
                return;
            }
        }
    }

    private boolean hasCallPermission() {

        // Check if permission is not granted
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

            return false; //checking -- wait for dialog

        }

        return true; // already
    }

    private void updatePhotoView(){
        if(photoFile == null || !photoFile.exists()){
            photoView.setImageDrawable(null);
        }
        else
        {
            Bitmap bitmap = PictureUtile.getScaledBitmap( photoFile.getPath(), getActivity());

            photoView.setImageBitmap(bitmap);
        }
    }


    private String getFormattedDate(Date crimeDate) {
        return new SimpleDateFormat("dd MMMM yyyy").format(crimeDate);
    }

    private String getFormattedTime(Date crimeDate) {
        return new SimpleDateFormat("hh:mm a").format(crimeDate);
    }
}