package com.example.asia.jmpro.logic.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.asia.jmpro.MainMenuPlaces;
import com.example.asia.jmpro.R;
import com.example.asia.jmpro.data.db.PlaceDao;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by asia on 25/10/2017.
 *
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    PlaceDao placeDao;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if(intent.getAction().equals("com.example.asia.jmpro.GET_NOTIFICATION")) {
            placeDao = new PlaceDao(context);
            String[] nearestPlacesList = placeDao.getNearestFavouritePlaces(intent.getDoubleExtra("longitude",0),intent.getDoubleExtra("latitude",0));
            if(nearestPlacesList.length != 0) {
                createBigNotification(nearestPlacesList);
            }

        }
        Log.e("NOTIFICATIONBROADCA", "onReceive: RECEIVED ");
    }


    protected void createBigNotification(String[] msgPositions) {
        Intent intent = new Intent(context, MainMenuPlaces.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(context.getString(R.string.invitation));
        for (int i=0; i < msgPositions.length; i++) {
            inboxStyle.addLine(msgPositions[i]);
        }

        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.places_fav_info))
                .setContentText(context.getString(R.string.places_fav_info))
                .setTicker(context.getString(R.string.fav_places))
                .setNumber(msgPositions.length)
                .setAutoCancel(true)
                .setStyle(inboxStyle)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, noti);
    }
}
