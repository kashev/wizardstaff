package com.wizardstaff;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.straphq.strapkit.strapkit_lib.messaging.StrapKitMessageService;

public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // This is all you need for an already developed app
        Intent serviceIntent = new Intent(this, StrapKitMessageService.class);
        startService(serviceIntent);

        // This is for the testing of StrapKit JS
        initializeViews();
    }

    private EditText mNotificationText;
    private Button mSendNotification;

    private void initializeViews() {

        mNotificationText = (EditText) findViewById(R.id.notification_edit_text);
        mSendNotification = (Button) findViewById(R.id.notification_button);

        mSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

    }

    private String getNotificationText() {
        String notificationText = mNotificationText.getText().toString();
        if (TextUtils.isEmpty(notificationText)) {
            notificationText = "Congrats on your first wearable notification!";
        }
        return notificationText;
    }

    private void sendNotification() {
        int notificationId = 001;

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("StrapKit JS")
                        .setContentText(getNotificationText());

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
