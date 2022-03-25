package com.example.mediacalremider.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mediacalremider.DialogActitvity;

public class DialogBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String medId = intent.getStringExtra("medId");
        String medName = intent.getStringExtra("medName");
        String medStrength = intent.getStringExtra("medStrength");
        int medPills = intent.getIntExtra("medPill",1);


        Intent showDialogIntent = new Intent(context, DialogActitvity.class);
        showDialogIntent.putExtra("medId",medId);
        showDialogIntent.putExtra("medName",medName);
        showDialogIntent.putExtra("medStrength",medStrength);
        showDialogIntent.putExtra("medPill",medPills);
        showDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(showDialogIntent);
        context.unregisterReceiver(ReminderWorker.receiver);


    }
}