package com.sbgmail.mateatominovic.mydogapp.activities.dataModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Enigma on 10.9.2017..
 */
public class DogInfo {

    @SerializedName("name")
    String name;

    @SerializedName("opis")
    String opis;

    @SerializedName("starost")
    int starost;

    @SerializedName("birthdate")
    String birthdate;

    @SerializedName("vrsta")
    String vrsta;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getStarost() {
        return starost;
    }

    public void setStarost(int starost) {
        this.starost = starost;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }
}
