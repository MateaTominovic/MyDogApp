package com.sbgmail.mateatominovic.mydogapp.activities.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Enigma on 11.9.2017..
 */
public class LocationData {
    @SerializedName("metaData")
    ArrayList<SingleLocation> locationArrayList;

    public ArrayList<SingleLocation> getLocationArrayList() {
        return locationArrayList;
    }

    public void setLocationArrayList(ArrayList<SingleLocation> locationArrayList) {
        this.locationArrayList = locationArrayList;
    }
}
