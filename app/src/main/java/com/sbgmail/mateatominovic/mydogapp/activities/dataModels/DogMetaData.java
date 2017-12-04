package com.sbgmail.mateatominovic.mydogapp.activities.dataModels;

import java.util.Comparator;

/**
 * Created by Enigma on 10.9.2017..
 */
public class DogMetaData  implements Comparator<DogMetaData> {
    String timestamp;
    String opis;
    int type;
    boolean isDone;
    long timestampMillis;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public void setTimestampMillis(long timestampMillis) {
        this.timestampMillis = timestampMillis;
    }

    //sortiranje itema tako da usporeduje lijevi i desni item te im mjenja pozicije po potrebi
    @Override
    public int compare(DogMetaData lhs, DogMetaData rhs) {
        if(lhs.getTimestampMillis() > rhs.getTimestampMillis()){
            return 1; // desni veci od lijevoga
        } else if(lhs.getTimestampMillis() < rhs.getTimestampMillis()){
            return -1; // lijevi manji od desnoga
        } else {
            return 0; // ako je jednak
        }
    }
}
