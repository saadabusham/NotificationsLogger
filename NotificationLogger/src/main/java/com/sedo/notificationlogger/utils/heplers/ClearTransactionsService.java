package com.sedo.notificationlogger.utils.heplers;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.sedo.notificationlogger.data.models.LoggerContentProvider;

public class ClearTransactionsService extends IntentService {

    public ClearTransactionsService() {
        super("Notification Logger-ClearTransactionsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getContentResolver().delete(LoggerContentProvider.TRANSACTION_URI, null, null);
        NotificationHelper.clearBuffer();
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.dismiss();
    }
}