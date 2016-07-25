package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Theerawuth on 7/18/2016.
 */
public class CrimeListFragment extends Fragment {

    private static final int REQUEST_UPDATED_CRIME = 200;

    private CrimeAdapter adapter;

    private  RecyclerView crimeRecycleView;

    protected  static final String TAG = "CRIME_LIST";

    private int crimePos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        crimeRecycleView = (RecyclerView) v.findViewById(R.id.crime_recycle_view);
        crimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));


        updateUI();

        return v;
    }

    /**
     * Update UI
     */
    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.getInstance();
        List<Crime> crimes = crimeLab.getCrime();

        if(adapter == null)
        {
            adapter = new CrimeAdapter(crimes);
            crimeRecycleView.setAdapter(adapter);
        }
        else
        {
            adapter.notifyItemChanged(crimePos);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resume list");
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_UPDATED_CRIME){
            if(resultCode == Activity.RESULT_OK){
                crimePos = (int) data.getExtras().get("position");
                Log.d(TAG, "get crimePos =" + crimePos);
            }


            Log.d(TAG, "Return from CrimeFragment");
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView;
        public TextView dateTextView;
        public CheckBox solvedCheckBox;
        Crime _crime;
        int _position;


        public CrimeHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            dateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            solvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);

            itemView.setOnClickListener(this);

        }

        public void bind(Crime crime, int position) {
            _crime = crime;
            _position = position;
            titleTextView.setText(_crime.getTitle());
            dateTextView.setText(_crime.getCrimeDate());
            solvedCheckBox.setChecked(_crime.getSolved());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimeActivity.newIntent(getActivity(), _crime.getId(), _position);
            startActivityForResult(intent,REQUEST_UPDATED_CRIME );

        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> crimes;
        private int viewCreatingCount;

        public CrimeAdapter(List<Crime> crimes){
            this.crimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            viewCreatingCount++;
            Log.d(TAG, "CREATE VIEW HOLDER FOR CRIMELIST : CREATING VIEW TIME= "+ viewCreatingCount);

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(v);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Log.d(TAG, "BIND VIEW HOLDER FOR CRIMELIST : POSITION = " + position);

            Crime crime = crimes.get(position);
            holder.bind(crime, position);


        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }
    }
}
