package com.sedo.notificationlogger.utils;

import android.content.Context;
import android.content.Intent;

import com.sedo.notificationlogger.ui.logger.MainActivity;

public class NotificationLogger {

    /**
     * Get an Intent to launch the Notification Logger UI directly.
     *
     * @param context A Context.
     * @return An Intent for the main Notification Logger Activity that can be started with {@link Context#startActivity(Intent)}.
     */
    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
