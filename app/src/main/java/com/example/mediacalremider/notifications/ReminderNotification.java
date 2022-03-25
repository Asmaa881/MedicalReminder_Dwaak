package com.example.mediacalremider.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.drug_details.DrugDetailsActivity;
import com.example.mediacalremider.requests.RequestListActivity;

public class ReminderNotification extends ContextWrapper{

    public static final String channel_id_med_reminder="medicine Reminder";
    public static final String channel_name_med_reminder ="channel1";
    public static final String channel_id_refill_reminder ="medicine refill Reminder";
    public static final String channel_name_refill_reminder ="channel2";
    public static final String channel_id_missed_reminder ="medicine missed Reminder";
    public static final String channel_name_missed_reminder ="channel3";

    public static final String channel_id_request_reminder ="medicine request Reminder";
    public static final String channel_name_request_reminder ="channel4";

    private NotificationManager notificationManager;

    public ReminderNotification(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            createReminderNotificationChannel();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createReminderNotificationChannel() {
        //// Medicine Reminder Channel
        NotificationChannel medReminderChannel=new NotificationChannel(channel_id_med_reminder,channel_name_med_reminder, NotificationManager.IMPORTANCE_DEFAULT);
        medReminderChannel.enableLights(true);
        medReminderChannel.enableVibration(true);
        medReminderChannel.setLightColor(R.color.teal_700);
        medReminderChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(medReminderChannel);
        //// Refill Channel
        NotificationChannel medRefillChannel=new NotificationChannel(channel_id_refill_reminder,channel_name_refill_reminder, NotificationManager.IMPORTANCE_DEFAULT);
        medRefillChannel.enableLights(true);
        medRefillChannel.enableVibration(true);
        medRefillChannel.setLightColor(R.color.teal_700);
        medRefillChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(medRefillChannel);
        ///// Missed Notification
        NotificationChannel medMissedChannel=new NotificationChannel(channel_id_missed_reminder,channel_name_missed_reminder, NotificationManager.IMPORTANCE_DEFAULT);
        medRefillChannel.enableLights(true);
        medRefillChannel.enableVibration(true);
        medRefillChannel.setLightColor(R.color.white);
        medRefillChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(medMissedChannel);
        //
        ///// Missed Notification
        NotificationChannel requestChannel =new NotificationChannel(channel_id_request_reminder,channel_name_request_reminder, NotificationManager.IMPORTANCE_DEFAULT);
        medRefillChannel.enableLights(true);
        medRefillChannel.enableVibration(true);
        medRefillChannel.setLightColor(R.color.white);
        medRefillChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(requestChannel);



    }
    public NotificationManager getManager(){
        if(notificationManager==null){
            notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getMedReminderChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(),channel_id_med_reminder)
                .setContentTitle("Reminder")
                .setContentText("That the time to take your medicine")
                .setSmallIcon(R.drawable.ic_alarm)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }
    public NotificationCompat.Builder getRequestChannelNotification(){
      Intent intent = new Intent(getApplicationContext(), RequestListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),15,intent,0);
        return new NotificationCompat.Builder(getApplicationContext(),channel_id_request_reminder)
                .setContentTitle("New Request")
                .setContentText("You have new request..")
                .setSmallIcon(R.drawable.ic_notifications)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
               .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getRefillReminderChannelNotification(){
       // Intent intent = new Intent(getApplicationContext(), DrugDetailsActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      //  PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),10,intent,0);
        return new NotificationCompat.Builder(getApplicationContext(),channel_id_refill_reminder)
                .setContentTitle("Reminder")
                .setContentText("Careful.. that the time to refill your pills..")
                .setSmallIcon(R.drawable.ic_notifications)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             //   .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }
    public NotificationCompat.Builder getMedMissedChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(),channel_id_missed_reminder)
                .setContentTitle("Reminder")
                .setContentText("That the time to take your medicine")
                .setSmallIcon(R.drawable.ic_alarm)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }



}
