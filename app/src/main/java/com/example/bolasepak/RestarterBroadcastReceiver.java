package com.example.bolasepak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, StepCounterService.class));
    }
}
