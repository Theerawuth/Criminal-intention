package com.augmentis.ayp.crimin;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = "SingleFragmentActivity";

    @LayoutRes
    protected int getLayoutResID(){
        return
                R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        Log.d(TAG, "On Create Activity");

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if ( f == null)
        {
            f = onCreateFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
            Log.d(CrimeListFragment.TAG ,"Fragment is created");
        }
        else
        {
            Log.d(CrimeListFragment.TAG, "Fragment have already been created");
        }
    }

    protected abstract Fragment onCreateFragment();
}
