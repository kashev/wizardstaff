package com.daranguiz.wizardstaff;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PollSparkService extends Service {
    public PollSparkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
