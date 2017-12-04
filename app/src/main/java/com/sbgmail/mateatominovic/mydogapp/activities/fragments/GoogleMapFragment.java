package com.sbgmail.mateatominovic.mydogapp.activities.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sbgmail.mateatominovic.mydogapp.R;
import com.sbgmail.mateatominovic.mydogapp.activities.activities.HomeActivity;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.SingleLocation;
import com.sbgmail.mateatominovic.mydogapp.activities.utility.UtilTime;

import java.util.ArrayList;

/**
 * Created by Enigma on 11.9.2017..
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback{

    // tipovi za boje pinova
    public static final int TYPE_VETERINAR = 0;
    public static final int TYPE_PARK = 1;
    public static final int TYPE_VFRIZER = 2;

    //globalna varijabla za google map
    GoogleMap googleMap;
    HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity(); // dohvacanje reference activitya
        return inflater.inflate(R.layout.activity_google_map, container, false); // postavljanje view-a
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // nađemo fragment po ID koji se nalazi u xml-u
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map); // dodaje kartu u fragment
        mapFragment.getMapAsync(this); // dohvacanje mape s neta
    }

    // kada dohvati kartu pokrene se ova metoda
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // vrši se provjera postoji li dopustenje za gps
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // prikazuje našu lokaciju
        googleMap.setMyLocationEnabled(true);
        // dodaje pinove na mapu
        setUpLocationPins(googleMap);
    }

    private void setUpLocationPins(GoogleMap googleMap) {
        // dohvaca podatke o pinovima
        ArrayList<SingleLocation> singleLocationArrayList = homeActivity.getLocationData().getLocationArrayList();

        // radi se loop kroz listu
        for(SingleLocation singleLocation : singleLocationArrayList){
            googleMap.addMarker(new MarkerOptions() // kreira marker
            .position(new LatLng(singleLocation.getLat(), singleLocation.getLng())) // koordinate za marker
            .title(singleLocation.getOpis()) // naslov markera
            .icon(BitmapDescriptorFactory.fromResource(getPinDrawable(singleLocation.getType())))); // dodavanje ikone
        }
        // zumiranje mape
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.549079, 18.695643), 12.0f));
    }

    // sluzi za uzimanje pina tj. za uzimanje ikone pina prema tipu u objektu
    private int getPinDrawable(int pinType) {
        switch (pinType){
            case TYPE_VETERINAR:
                return R.drawable.pin_veterinar;

            case TYPE_PARK:
                return R.drawable.pin_park;

            case TYPE_VFRIZER:
                return R.drawable.pin_frizer;

            default:
                return R.drawable.pin_restac;
        }
    }
}
