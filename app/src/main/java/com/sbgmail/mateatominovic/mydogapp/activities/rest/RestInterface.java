package com.sbgmail.mateatominovic.mydogapp.activities.rest;

import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.Confing;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogData;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogInfo;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.LocationData;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.Notification;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Enigma on 10.9.2017..
 */
public interface RestInterface {

    @FormUrlEncoded
    @POST("login")
    Call<Confing> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("logout")
    Call<Confing> logout(@Field("token") String token);

    @FormUrlEncoded
    @POST("mydog-cijepljenje")
    Call<DogData> getCijepljenje(@Field("token") String token);

    @FormUrlEncoded
    @POST("mydog-hranjenje")
    Call<DogData> getHranjenje(@Field("token") String token);

    @FormUrlEncoded
    @POST("mydog-setnja")
    Call<DogData> getSetnja(@Field("token") String token);

    @FormUrlEncoded
    @POST("mydog-info")
    Call<DogInfo> getDogInfoData(@Field("token") String token);

    @FormUrlEncoded
    @POST("mydog-lokacije")
    Call<LocationData> getLocationData(@Field("token") String token);

    @FormUrlEncoded
    @POST("mydog-notifikacije")
    Call<Notification> getNotifications(@Field("token") String token);
}
