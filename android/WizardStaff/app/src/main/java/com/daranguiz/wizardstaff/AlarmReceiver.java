package com.daranguiz.wizardstaff;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    String TAG = "AlarmReceiver";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Start service to poll spark
//        Log.d(TAG, "Starting PollSparkService");
        Intent service = new Intent(context, PollGameStatusService.class);
        startWakefulService(context, service);
    }
}