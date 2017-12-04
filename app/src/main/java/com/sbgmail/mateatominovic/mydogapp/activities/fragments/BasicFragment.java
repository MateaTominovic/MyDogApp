package com.sbgmail.mateatominovic.mydogapp.activities.fragments;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sbgmail.mateatominovic.mydogapp.R;
import com.sbgmail.mateatominovic.mydogapp.activities.ListViewAdapter;
import com.sbgmail.mateatominovic.mydogapp.activities.OneTimeReciveNotification;
import com.sbgmail.mateatominovic.mydogapp.activities.activities.HomeActivity;
import com.sbgmail.mateatominovic.mydogapp.activities.activities.LoginActivity;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogMetaData;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Enigma on 10.9.2017..
 */
public class BasicFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    // globalne variable
    HomeActivity homeActivity;
    ArrayList<DogMetaData> dogMetaDataArrayList;
    RelativeLayout rlSwitchContainer;
    TextView tvSwitch;
    Switch swSwitch;
    TextView tvTitle;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity(); // castanje Activitya u HomeActivity
        return inflater.inflate(R.layout.home_fragment_layout, container, false); // dodavanje UI na fragment
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // pozivamo onViewCreated od extendanog fragmenta
        super.onViewCreated(view, savedInstanceState);
        // stvaranje objekata iz UI tj XML
        listView = (ListView) view.findViewById(R.id.lv_list_view);
        rlSwitchContainer = (RelativeLayout) view.findViewById(R.id.rl_container);
        tvSwitch = (TextView) view.findViewById(R.id.tv_switch);
        swSwitch = (Switch) view.findViewById(R.id.sw_switch);

        dogMetaDataArrayList = homeActivity.getDogData().getDogMetaData(); // dohvacanje podataka
        ListViewAdapter listViewAdapter = new ListViewAdapter(dogMetaDataArrayList, homeActivity.getDogData().getType(), getContext()); // inicijalizacija adaptera
        listView.setAdapter(listViewAdapter); // povezuje se adapter s view-om
        // prikazi switch ako je fragment notification tipa kako bi se mogle ukljucit/iskljucit notifikacije
        if(homeActivity.getDogData().getType() == HomeActivity.TYPE_MY_NOTIFICATION){
            rlSwitchContainer.setVisibility(View.VISIBLE);
            // postavljanje listenera kako bi se javio event checkiranja i uncheckiranja
            swSwitch.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            // ako je čekiran napravi notifikacije
            makeNotification();
            //promjeni text na ukljuceno
            tvSwitch.setText("Uključeno");
        } else {
            // ako je iskljuceno makni alarme
            removeAlarms();
            tvSwitch.setText("Isključeno");
        }
    }

    private void makeNotification() {
        // getanje alarm managera
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        for(int i = 0; i < dogMetaDataArrayList.size(); i++){

            DogMetaData dogMetaData = dogMetaDataArrayList.get(i);
            // kreiramo intent
            Intent intent = new Intent(getContext(), OneTimeReciveNotification.class); //OneTimeReciveNotification.class kaze gdje se taj intent treba dostavit
            //spremamo podatke koji ce nam trebat u OneTimeRecieveru u intent
            Bundle args = new Bundle();
            args.putInt("REQUEST_CODE", i);
            args.putString("opis", dogMetaData.getOpis());
            intent.putExtras(args);
            // kreiramo pending intent koji ce se dostaviti u OneTimeReciveNotification kada dode vrijeme za to
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // uzimamo vrijeme kada ga treba okinuti u milisekundama
            long timeInMillis = dogMetaData.getTimestampMillis();
            // provjeravamo ako je vrijeme te notifikacije u proslosti onda ga ne trebam kreirat, ako je u buducnost kreiraj ga
            if(Calendar.getInstance().getTimeInMillis() < timeInMillis){
                // salje alarm alarm manageru
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
        }
    }

    public void removeAlarms (){
        // potrebno je kreirati pending intent s istim paremetrima te ga treba poslati alarmManageru na cancel
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < dogMetaDataArrayList.size(); i++){
            Intent recreateIntent = new Intent(getContext(), OneTimeReciveNotification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), i, recreateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }
    }
}
