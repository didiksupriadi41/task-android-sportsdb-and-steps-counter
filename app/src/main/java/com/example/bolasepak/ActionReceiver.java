package com.example.bolasepak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, StepCounterService.class);
        context.stopService(i);
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
}
