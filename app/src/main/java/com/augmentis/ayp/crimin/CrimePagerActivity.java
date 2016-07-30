package com.augmentis.ayp.crimin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager _viewPager;
    private List<Crime> _crime;
    private UUID _crimeID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        _crimeID = (UUID) getIntent().getSerializableExtra(CRIME_ID);

        _viewPager = (ViewPager) findViewById(R.id.activity_crime_view_pager);

        _crime = CrimeLab.getInstance(this).getCrime();

        FragmentManager fm = getSupportFragmentManager();

        _viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = _crime.get(position);
                Fragment f = CrimeFragment.newInstance(crime.getId(), position);
                return f;
            }

            @Override
            public int getCount() {
                return _crime.size();
            }
        });

        //set position
        int position = CrimeLab.getInstance(this).getCrimePositionById(_crimeID);
        _viewPager.setCurrentItem(position);
    }

        protected static final String CRIME_ID = "crimeActivity.crimeId";

        public static Intent newIntent(Context activity, UUID id){
            Intent intent = new Intent(activity, CrimePagerActivity.class);
            intent.putExtra(CRIME_ID, id);
            return intent;
        }

    }

