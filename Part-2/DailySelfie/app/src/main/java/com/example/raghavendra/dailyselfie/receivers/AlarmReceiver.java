package com.example.raghavendra.dailyselfie.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.raghavendra.dailyselfie.R;
import com.example.raghavendra.dailyselfie.activities.MainActivity;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_photo_camera)
                .setContentText("It's time for a selfie!")
                .setContentTitle("DailySelfie")
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, notificationBuilder.build());

    }
}
