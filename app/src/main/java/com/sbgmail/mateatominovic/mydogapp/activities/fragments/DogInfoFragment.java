package com.sbgmail.mateatominovic.mydogapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbgmail.mateatominovic.mydogapp.R;
import com.sbgmail.mateatominovic.mydogapp.activities.activities.HomeActivity;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogInfo;
import com.sbgmail.mateatominovic.mydogapp.activities.utility.UtilTime;

import java.text.ParseException;

/**
 * Created by Enigma on 10.9.2017..
 */
public class DogInfoFragment extends Fragment {
    // globlne variable
    TextView tvName;
    TextView tvOpis;
    TextView tvStaros;
    TextView tvBirthDate;
    TextView tvVrsta;
    HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity(); // casta activity u HomeActivity
        return inflater.inflate(R.layout.dog_info_layout, container, false); // dodaje UI na fragment
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // stvaranje UI objekata iz xml
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvOpis = (TextView) view.findViewById(R.id.tv_opis);
        tvStaros = (TextView) view.findViewById(R.id.tv_starost);
        tvBirthDate = (TextView) view.findViewById(R.id.tv_birthdate);
        tvVrsta = (TextView) view.findViewById(R.id.tv_vrsta);

        // dohvaca podatke iz aktivitya
        DogInfo dogInfo = homeActivity.getDogInfo(); // vraca podatke o psu

        // ispisuje podatke iz dogInfa - getDogInfo
        tvName.setText(getString(R.string.name) + dogInfo.getName());
        tvOpis.setText(getString(R.string.description) + dogInfo.getOpis());
        tvStaros.setText(getString(R.string.age) + dogInfo.getStarost());
        try {
            tvBirthDate.setText(getString(R.string.date) + UtilTime.getFormatDateFromTimeStamp(dogInfo.getBirthdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvVrsta.setText(getString(R.string.breed) + dogInfo.getVrsta());
    }
}
