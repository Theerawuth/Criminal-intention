package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

/**
 * Created by Theerawuth on 7/18/2016.
 */
public class CrimeListFragment extends Fragment {

    private static final int REQUEST_UPDATED_CRIME = 200;
    private static final String SUBTITLE_VISIBLE_STATE = "Subtitle";

    private CrimeAdapter adapter;

    private  RecyclerView crimeRecycleView;

    protected  static final String TAG = "CRIME_LIST";

    private boolean _subtitleVisible;

    private TextView showText;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        crimeRecycleView = (RecyclerView) v.findViewById(R.id.crime_recycle_view);
        crimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity())); //สร้างLayoutให้กับRecycleViewเพื่อส่งมันไปใช้
        showText = (TextView) v.findViewById(R.id.show_text);



        if(savedInstanceState != null){
            _subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_STATE);
        }

        updateUI();

        return v;
    }


    /**
     * Update UI
     */
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrime();
        if(crimes.isEmpty()){
            showText.setVisibility(View.VISIBLE);
        }
        else
        {
            showText.setVisibility(View.INVISIBLE);
        }

        if (adapter == null) {
            adapter = new CrimeAdapter(crimes);
            crimeRecycleView.setAdapter(adapter);
        } else {
            adapter.setCrimes(crimeLab.getCrime());
            adapter.notifyDataSetChanged();
        }

        updateSubTitle();
    }

    // Set Menu ALL
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set option menu
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_menu, menu);

        //Get Menuitem: Subtitle
        MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(_subtitleVisible)
        {
            menuItem.setTitle(R.string.hide_subtitle);
        }
        else
        {
            menuItem.setTitle(R.string.show_subtitle);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Log.d(TAG, "ADD :");
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime); //TODO AddCrime() to Crime
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true; // จะไม่มีอะไรทำต่อจากนี้แล้วหรือ Success

            case R.id.menu_item_show_subtitle:
                _subtitleVisible = !_subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubTitle();
                return true;

            //default case
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubTitle(){
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrime().size();
        String subTitle = getString(R.string.subtitle_format, crimeCount);

        //plurals
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_format, crimeCount, crimeCount);


        if(!_subtitleVisible){
            subTitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SUBTITLE_VISIBLE_STATE, _subtitleVisible);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resume list");
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String pathPicture;
        public TextView titleTextView;
        public TextView dateTextView;
        public CheckBox solvedCheckBox;
        Crime _crime;
        int _position;
        UUID _crimeId;


        public CrimeHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            dateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            solvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
            solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
                    Crime crime = crimeLab.getCrimeById(_crime.getId());
                    crime.setSolved(isChecked);
                    crimeLab.updateCrime(crime);
                }
            });

            itemView.setOnClickListener(this);

        }

        public void bind(Crime crime, int position) {
            _crimeId = crime.getId();
            _crime = crime;
            _position = position;
            titleTextView.setText(_crime.getTitle());
            dateTextView.setText(_crime.getCrimeDate().toString());
            solvedCheckBox.setChecked(_crime.getSolved());
        }

        @Override
        public void onClick(View v) {

            Intent intent = CrimePagerActivity.newIntent(getActivity(), _crime.getId());
            startActivityForResult(intent,REQUEST_UPDATED_CRIME );

        }








    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> _crimes;
        private int viewCreatingCount;
        public CrimeAdapter(List<Crime> crimes){

            _crimes = crimes;
        }

        protected void setCrimes(List<Crime> crimes) {
            _crimes = crimes;
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

            Crime crime = _crimes.get(position);
            holder.bind(crime, position);
        }

        @Override
        public int getItemCount() {
            return _crimes.size();
        }
    }
}
