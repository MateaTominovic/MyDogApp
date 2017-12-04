package com.sbgmail.mateatominovic.mydogapp.activities.rest;

import android.content.Context;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Enigma on 10.9.2017..
 */
public class RestProvider {

    public static final String BASE_URL = "http://dogee.herokuapp.com/"; //base url
    private static RestInterface service;

    // radi API Singleton (singleton pattern) - to je objekt koji se instancira samo jednom i živi dok je cijela aplikacije živa
    public static RestInterface getInterface(Context context){

        if(service == null){
            createService(context.getApplicationContext());
        }
            return service;
    }

    public static void createService(Context context){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // setapiranje loganja requestova... Logaj mi BODY responsa

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder(); // vodi računa o konekciji i hendla networking komunikaciju etc.
        okHttpClient.addInterceptor(httpLoggingInterceptor); // dodavanje intereseptor na okHttpKlijent

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // posatavlja se base url
                .client(okHttpClient.build()) // dodan http klijent
                .addConverterFactory(GsonConverterFactory.create(new Gson())) // definira način na koji ce se response pretvarat u objekt
                .build();

        service = retrofit.create(RestInterface.class);
    }
}
