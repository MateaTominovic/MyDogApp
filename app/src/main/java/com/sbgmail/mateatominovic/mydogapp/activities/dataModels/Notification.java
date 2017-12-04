package com.sbgmail.mateatominovic.mydogapp.activities.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Enigma on 12.9.2017..
 */
public class Notification {

    @SerializedName("metaData")
    NotificationMetaData metaData;

    int type;

    public NotificationMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(NotificationMetaData metaData) {
        this.metaData = metaData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
