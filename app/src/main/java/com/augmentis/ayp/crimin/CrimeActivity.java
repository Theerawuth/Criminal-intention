package com.augmentis.ayp.crimin;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    protected static final String CRIME_ID = "crimeActivity.crimeId";
    protected static final String CRIME_POSITION = "crimeActivity.crimePos";


    public static Intent newIntent(Context context, UUID id, int _position){
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(CRIME_ID, id);
        intent.putExtra(CRIME_POSITION, _position);
        return intent;
    }

    @Override
    protected Fragment onCreateFragment() {
        UUID crimeID = (UUID) getIntent().getSerializableExtra(CRIME_ID);
        Fragment fragment = CrimeFragment.newInstance(crimeID);
        return fragment;
    }
}
