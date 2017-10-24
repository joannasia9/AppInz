package com.example.asia.jmpro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.example.asia.jmpro.data.db.PlaceDao;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by asia on 24/10/2017.
 */

public class NotificationCheckerAsyncTask extends AsyncTask<Location, Void, String[]> {
    private Context context;
    private PlaceDao placeDao;

   public NotificationCheckerAsyncTask(Context context){
        this.context = context;
        this.placeDao = new PlaceDao(context);
    }

    @Override
    protected String[] doInBackground(Location... params) {
        return placeDao.getNearestFavouritePlaces(params[0]);
    }

    @Override
    protected void onPostExecute(String[] strings) {
        if(strings.length!=0) {
            createBigNotification(strings);
        }
        super.onPostExecute(strings);
    }

    private void createBigNotification(String[] msgPositions) {
        Intent intent = new Intent(context, MainMenuPlaces.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Znajdujesz się w pobliżu ulubioych miejsc:");
        for (String msgPosition : msgPositions) {
            inboxStyle.addLine(msgPosition);
        }

        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle("Nowa wiadomość")
                .setContentText("Temat wiadomości")
                .setTicker("Masz wiadomość")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.item1))
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, noti);
    }
}
