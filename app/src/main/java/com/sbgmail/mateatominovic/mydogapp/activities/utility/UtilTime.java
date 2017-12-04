package com.sbgmail.mateatominovic.mydogapp.activities.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Enigma on 11.9.2016..
 */
public class UtilTime {
    // utiliti klasa je klasa s static metodama. Koja sluzi za orbadu nekih podataka i njena prednost nad pbicnom klasom je ta sto se ne mora instacirati objekt te klase da bi se pristupilo metodi
    public static String getFormatTimeFromTimeStamp(String timeStamp) throws ParseException {
        //Format example 8-2-2016T20:33
        SimpleDateFormat oldTimeFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm"); // format time stempa kojeg primas
        SimpleDateFormat newTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm"); // format outputa kojeg zelimo
        Date date = oldTimeFormat.parse(timeStamp); // parsira podatak o vremenu i od njega radi date objekt
        return newTimeFormat.format(date); // date objekt formatira u zeljeni format outputa
    }

    public static String getFormatDateFromTimeStamp(String timeStamp) throws ParseException {
        //Format example 8-2-2016T20:33
        SimpleDateFormat oldTimeFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm"); // format time stempa kojeg primas
        SimpleDateFormat newTimeFormat = new SimpleDateFormat("dd.MM.yyyy"); // format outputa kojeg zelimo
        Date date = oldTimeFormat.parse(timeStamp); // parsira podatak o vremenu i od njega radi date objekt
        return newTimeFormat.format(date); // date objekt formatira u zeljeni format outputa
    }

    public static boolean isDone(long timestampMillis) {
        long currentMillis = System.currentTimeMillis();
        if(currentMillis > timestampMillis * 1000)
            return true;
        else
            return false;
    }
}
