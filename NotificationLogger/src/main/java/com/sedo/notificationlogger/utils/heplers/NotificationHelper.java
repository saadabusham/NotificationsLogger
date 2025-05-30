package com.sedo.notificationlogger.utils.heplers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.LongSparseArray;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.sedo.notificationlogger.R;
import com.sedo.notificationlogger.data.models.LoggerModel;
import com.sedo.notificationlogger.ui.logger.BaseActivity;
import com.sedo.notificationlogger.utils.NotificationLogger;

import java.lang.reflect.Method;

public class NotificationHelper {

    private static final String CHANNEL_ID = "notificationlogger";
    private static final int NOTIFICATION_ID = 11385;
    private static final int BUFFER_SIZE = 10;

    private static final LongSparseArray<LoggerModel> transactionBuffer = new LongSparseArray<>();
    private static int transactionCount;

    private final Context context;
    private final NotificationManager notificationManager;
    private Method setChannelId;

    public static synchronized void clearBuffer() {
        transactionBuffer.clear();
        transactionCount = 0;
    }

    private static synchronized void addToBuffer(LoggerModel transaction) {
        if (Integer.parseInt(transaction.getStatus()) == LoggerModel.Status.Requested.getMode()) {
            transactionCount++;
        }
        transactionBuffer.put(transaction.getId(), transaction);
        if (transactionBuffer.size() > BUFFER_SIZE) {
            transactionBuffer.removeAt(0);
        }
    }

    public NotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID,
                            context.getString(R.string.notification_category), NotificationManager.IMPORTANCE_LOW));
            try {
                setChannelId = NotificationCompat.Builder.class.getMethod("setChannelId", String.class);
            } catch (Exception ignored) {}
        }
    }

    public synchronized void show(LoggerModel transaction) {
        addToBuffer(transaction);
        if (!BaseActivity.isInForeground()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setContentIntent(PendingIntent.getActivity(context, 0, NotificationLogger.getLaunchIntent(context), PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE))
                    .setLocalOnly(true)
                    .setSmallIcon(R.drawable.logger_ic_notification_white_24dp)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setContentTitle(context.getString(R.string.logger_notification_title));
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            if (setChannelId != null) {
                try { setChannelId.invoke(builder, CHANNEL_ID); } catch (Exception ignored) {}
            }
            int count = 0;
            for (int i = transactionBuffer.size() - 1; i >= 0; i--) {
                if (count < BUFFER_SIZE) {
                    if (count == 0) {
                        builder.setContentText(transactionBuffer.valueAt(i).getNotificationText());
                    }
                    inboxStyle.addLine(transactionBuffer.valueAt(i).getNotificationText());
                }
                count++;
            }
            builder.setAutoCancel(true);
            builder.setStyle(inboxStyle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setSubText(String.valueOf(transactionCount));
            } else {
                builder.setNumber(transactionCount);
            }
            builder.addAction(getClearAction());
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    @NonNull
    private NotificationCompat.Action getClearAction() {
        CharSequence clearTitle = context.getString(R.string.logger_clear);
        Intent deleteIntent = new Intent(context, ClearTransactionsService.class);
        PendingIntent intent = PendingIntent.getService(context, 11, deleteIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Action(R.drawable.logger_ic_delete_white_24dp,
                clearTitle, intent);
    }

    public void dismiss() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
