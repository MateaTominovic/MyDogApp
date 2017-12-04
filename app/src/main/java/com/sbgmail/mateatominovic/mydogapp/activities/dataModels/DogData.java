package com.sbgmail.mateatominovic.mydogapp.activities.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Enigma on 10.9.2017..
 */
public class DogData {

    @SerializedName("metaData")
    ArrayList<DogMetaData> metaData;

    int type;

    public ArrayList<DogMetaData> getDogMetaData() {
        return metaData;
    }

    public void setDogMetaData(ArrayList<DogMetaData> metaData) {
        this.metaData = metaData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
