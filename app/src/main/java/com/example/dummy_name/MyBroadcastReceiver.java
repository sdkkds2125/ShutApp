package com.example.dummy_name;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager.areNotificationsEnabled() || notificationManager.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_NONE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                notificationManager.setNotificationPolicy(new NotificationManager.Policy(NotificationManager.Policy.PRIORITY_CATEGORY_ALARMS,
                        NotificationManager.Policy.SUPPRESSED_EFFECT_PEEK |
                                NotificationManager.Policy.SUPPRESSED_EFFECT_LIGHTS |
                                NotificationManager.Policy.SUPPRESSED_EFFECT_BADGE |
                                NotificationManager.Policy.SUPPRESSED_EFFECT_AMBIENT,
                        0));
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                notificationManager.setNotificationPolicy(new NotificationManager.Policy(NotificationManager.Policy.PRIORITY_CATEGORY_ALARMS |
                        NotificationManager.Policy.PRIORITY_CATEGORY_MEDIA |
                        NotificationManager.Policy.PRIORITY_CATEGORY_SYSTEM |
                        NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS |
                        NotificationManager.Policy.PRIORITY_CATEGORY_EVENTS |
                        NotificationManager.Policy.PRIORITY_CATEGORY_CALLS |
                        NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES,
                        NotificationManager.Policy.PRIORITY_SENDERS_ANY,
                        NotificationManager.Policy.PRIORITY_SENDERS_ANY,
                        0,
                        NotificationManager.Policy.CONVERSATION_SENDERS_ANYONE)
                );
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }
        }

    }
}