package com.augmentis.ayp.crimin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import java.util.UUID;


public class CrimePagerActivity extends SingleFragmentActivity implements CrimeFragment.Callbacks {

    @Override
    protected int getLayoutResID() {
        return super.getLayoutResID();
    }

    private UUID _crimeId;

    @Override
    protected Fragment onCreateFragment() {
        _crimeId = (UUID) getIntent().getSerializableExtra(CRIME_ID);
        return CrimeFragment.newInstance(_crimeId);
    }

    protected static final String CRIME_ID = "crimePagerActivity.crimeId";

    public static Intent newIntent(Context activity, UUID id) {
        Intent intent = new Intent(activity, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID, id);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        // TODO I WILL SEE WHAT
    }
}
