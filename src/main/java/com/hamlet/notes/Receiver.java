package com.hamlet.notes;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Receiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {

        String description = intent.getStringExtra("DESCRIPTION");
        int pri = intent.getIntExtra("PRIORITY",0);

        int priority;
        if(pri == 1){
            priority = NotificationManager.IMPORTANCE_MAX;
        } else {
            priority = NotificationManager.IMPORTANCE_DEFAULT;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"ToDoChennel")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("To-Do Remainder")
                .setContentText(description)
                .setPriority(priority);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(200,builder.build());
    }
}
