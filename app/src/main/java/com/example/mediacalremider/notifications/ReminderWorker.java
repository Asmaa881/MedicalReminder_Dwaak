package com.example.mediacalremider.notifications;


import static android.content.Context.WINDOW_SERVICE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mediacalremider.R;

public class ReminderWorker extends Worker {

    ReminderNotification notification;
    public  static DialogBroadcastReceiver receiver;
    public ReminderWorker(@NonNull Context appContext, @NonNull WorkerParameters params) {
        super(appContext, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        notification = new ReminderNotification(getApplicationContext());
        Data data = getInputData();
        String medId = data.getString("medId");
        String medName = data.getString("medName");
        String medStrength = data.getString("medStrength");
        int medPills = data.getInt("medPill",1);
       // Log.i("getDialogData",medId);
       // Log.i("getDialogData",medName);
      //  Log.i("getDialogData",medStrength);
     //   Log.i("getDialogData",""+medPills);
        IntentFilter intentFilter=new IntentFilter("OpenDialog");
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        receiver =new DialogBroadcastReceiver();
        getApplicationContext().registerReceiver(receiver,intentFilter);
        Intent intent = new Intent();
        intent.putExtra("medId",medId);
        intent.putExtra("medName",medName);
        intent.putExtra("medStrength",medStrength);
        intent.putExtra("medPill",medPills);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("OpenDialog");
        getApplicationContext().sendBroadcast(intent);

        NotificationCompat.Builder builder = notification.getMedReminderChannelNotification();
        notification.getManager().notify(1,builder.build());

        return Result.success();
    }
    @Override
    public void onStopped() {
        super.onStopped();
    }

}
