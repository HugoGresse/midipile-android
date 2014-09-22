package fr.creads.midipile.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;

/**
 * Notify the user that he can participate
 *
 * Author : Hugo Gresse
 * Date : 18/09/14
 */
public class ParticipateNotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PendingIntent homeActivityPendingIntent =
                PendingIntent.getActivity(context, 0, new Intent( context, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getResources().getString(R.string.app_notification_title))
                        .setContentText(context.getResources().getString(R.string.app_notification_message))
                        .setContentIntent(homeActivityPendingIntent)
                        .setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

}
