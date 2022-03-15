package com.sedo.notificationlogger.utils.heplers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sedo.notificationlogger.data.models.LoggerContentProvider;
import com.sedo.notificationlogger.utils.NotificationLoggerSender;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RetentionManager{
        private static final String LOG_TAG = "Notification Logger";
        private static final String PREFS_NAME = "notification_logger_preferences";
        private static final String KEY_LAST_CLEANUP = "last_cleanup";

        private static long lastCleanup;

        private final Context context;
        private final long period;
        private final long cleanupFrequency;
        private final SharedPreferences prefs;

        public RetentionManager(Context context, NotificationLoggerSender.Period retentionPeriod) {
            this.context = context;
            period = toMillis(retentionPeriod);
            prefs = context.getSharedPreferences(PREFS_NAME, 0);
            cleanupFrequency = (retentionPeriod == NotificationLoggerSender.Period.ONE_HOUR) ?
                    TimeUnit.MINUTES.toMillis(30) : TimeUnit.HOURS.toMillis(2);
        }

        public synchronized void doMaintenance() {
            if (period > 0) {
                long now = new Date().getTime();
                if (isCleanupDue(now)) {
                    Log.i(LOG_TAG, "Performing data retention maintenance...");
                    deleteSince(getThreshold(now));
                    updateLastCleanup(now);
                }
            }
        }

        private long getLastCleanup(long fallback) {
            if (lastCleanup == 0) {
                lastCleanup = prefs.getLong(KEY_LAST_CLEANUP, fallback);
            }
            return lastCleanup;
        }

        private void updateLastCleanup(long time) {
            lastCleanup = time;
            prefs.edit().putLong(KEY_LAST_CLEANUP, time).apply();
        }

        private void deleteSince(long threshold) {
            int rows = context.getContentResolver().delete(LoggerContentProvider.TRANSACTION_URI,
                    "date <= ?", new String[] { String.valueOf(threshold) });
            Log.i(LOG_TAG, rows + " transactions deleted");
        }

        private boolean isCleanupDue(long now) {
            return (now - getLastCleanup(now)) > cleanupFrequency;
        }

        private long getThreshold(long now) {
            return (period == 0) ? now : now - period;
        }

        private long toMillis(NotificationLoggerSender.Period period) {
            switch (period) {
                case ONE_HOUR:
                    return TimeUnit.HOURS.toMillis(1);
                case ONE_DAY:
                    return TimeUnit.DAYS.toMillis(1);
                case ONE_WEEK:
                    return TimeUnit.DAYS.toMillis(7);
                default:
                    return 0;
            }
        }
}