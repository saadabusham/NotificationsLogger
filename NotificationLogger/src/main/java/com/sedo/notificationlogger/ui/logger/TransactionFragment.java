package com.sedo.notificationlogger.ui.logger;

import com.sedo.notificationlogger.data.models.LoggerModel;

interface TransactionFragment {
    void transactionUpdated(LoggerModel transaction);
}
