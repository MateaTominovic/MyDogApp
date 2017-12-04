package com.sbgmail.mateatominovic.mydogapp.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.sbgmail.mateatominovic.mydogapp.R;
import com.sbgmail.mateatominovic.mydogapp.activities.activities.LoginActivity;

/**
 * Created by Enigma on 11.9.2017..
 */
public class OneTimeReciveNotification extends BroadcastReceiver {
    // dohvaca poruke koje su odaslane od OS-a
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        int requestCode = extras.getInt("REQUEST_CODE", 1); // vadimo request code podatak iz intenta
        String opis = extras.getString("opis"); // vadimo podatak opisa iz intent

        intent = new Intent(context, LoginActivity.class);  // kreiraj intent i dostavi ga u LoginActivity
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); // kreiranje pending intenta

        //kreiranje sadrzaja norifikacije
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true) // kada kliknemo na notifikaciju da nestane
                .setWhen(System.currentTimeMillis()) // prikazi ju odma
                .setSmallIcon(R.drawable.notification) // setaj ikonu
                .setContentTitle("Vau Vau") // setaj title
                .setContentText(opis) // setaj opis
                .setDefaults(Notification.DEFAULT_SOUND) // dodan zvuk
                .setContentIntent(contentIntent) // stavlja pending intent da ceka na klik pa ce otvorit home Activity
                .setContentInfo(""); // seta "" na content info
        // geta Notification manager is OS-a
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // obavjstava notification manager o notifikaciji
        notificationManager.notify(requestCode, builder.build());

    }
}
