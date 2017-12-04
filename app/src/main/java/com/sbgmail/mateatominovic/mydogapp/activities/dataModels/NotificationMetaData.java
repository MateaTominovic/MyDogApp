package com.sbgmail.mateatominovic.mydogapp.activities.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Enigma on 12.9.2017..
 */
public class NotificationMetaData {
    @SerializedName("cijepljenje")
    ArrayList<DogMetaData> cijepljenjeArray;

    @SerializedName("hranjenje")
    ArrayList<DogMetaData> hranjenjeArray;

    @SerializedName("setnja")
    ArrayList<DogMetaData> setnjaArray;

    public ArrayList<DogMetaData> getCijepljenjeArray() {
        return cijepljenjeArray;
    }

    public void setCijepljenjeArray(ArrayList<DogMetaData> cijepljenjeArray) {
        this.cijepljenjeArray = cijepljenjeArray;
    }

    public ArrayList<DogMetaData> getHranjenjeArray() {
        return hranjenjeArray;
    }

    public void setHranjenjeArray(ArrayList<DogMetaData> hranjenjeArray) {
        this.hranjenjeArray = hranjenjeArray;
    }

    public ArrayList<DogMetaData> getSetnjaArray() {
        return setnjaArray;
    }

    public void setSetnjaArray(ArrayList<DogMetaData> setnjaArray) {
        this.setnjaArray = setnjaArray;
    }

    public  ArrayList<DogMetaData> getAllList(){
        ArrayList<DogMetaData> list = new ArrayList<>();
        list.addAll(this.cijepljenjeArray);
        list.addAll(this.hranjenjeArray);
        list.addAll(this.setnjaArray);
        return list;
    }
}
