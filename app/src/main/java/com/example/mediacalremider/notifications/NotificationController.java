package com.example.mediacalremider.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mediacalremider.MainActivity;
import com.example.mediacalremider.R;
import com.example.mediacalremider.requests.RequestListActivity;

public class NotificationController {

    public static void newRequestNotification(Context current,String channelId,int notificationId){
        Intent intent1= new Intent(current, RequestListActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(current,0,intent1,0);
        NotificationCompat.Builder notification=new NotificationCompat.Builder(current, channelId)
                .setSmallIcon(R.drawable.drug_icon)
                .setContentTitle("Dwaak")
                .setContentText("New Request for you")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(current);
        notificationManager.notify(notificationId,notification.build());
    }

    public static void createChannel(String channelId, Context context){
        CharSequence name= "Dwaak";
        String description= "Number Of Pills";
        int importance= NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel= new NotificationChannel(channelId,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager= context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
