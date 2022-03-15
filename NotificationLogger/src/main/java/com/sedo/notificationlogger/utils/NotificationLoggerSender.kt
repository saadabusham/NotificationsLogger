package com.sedo.notificationlogger.utils

import com.sedo.notificationlogger.utils.heplers.NotificationHelper
import com.sedo.notificationlogger.utils.heplers.RetentionManager
import com.sedo.notificationlogger.data.models.LoggerModel
import android.content.Context
import android.net.Uri
import com.sedo.notificationlogger.data.models.LocalCupboard
import com.sedo.notificationlogger.data.models.LoggerContentProvider
import java.nio.charset.Charset
import java.util.*

class NotificationLoggerSender(context: Context) {
    enum class Period {
        /**
         * Retain data for the last hour.
         */
        ONE_HOUR,

        /**
         * Retain data for the last day.
         */
        ONE_DAY,

        /**
         * Retain data for the last week.
         */
        ONE_WEEK,

        /**
         * Retain data forever.
         */
        FOREVER
    }

    private val context: Context
    private val notificationHelper: NotificationHelper
    private var retentionManager: RetentionManager
    private var showNotification: Boolean
    private var maxContentLength = 250000L

    /**
     * Control whether a notification is shown while HTTP activity is recorded.
     *
     * @param show true to show a notification, false to suppress it.
     * @return The [NotificationLoggerSender] instance.
     */
    fun showNotification(show: Boolean): NotificationLoggerSender {
        showNotification = show
        return this
    }

    /**
     * Set the maximum length for request and response content before it is truncated.
     * Warning: setting this value too high may cause unexpected results.
     *
     * @param max the maximum length (in bytes) for request/response content.
     * @return The [NotificationLoggerSender] instance.
     */
    fun maxContentLength(max: Long): NotificationLoggerSender {
        maxContentLength = max
        return this
    }

    /**
     * Set the retention period for HTTP transaction data captured by this interceptor.
     * The default is one week.
     *
     * @param period the peroid for which to retain HTTP transaction data.
     * @return The [NotificationLoggerSender] instance.
     */
    fun retainDataFor(period: Period?): NotificationLoggerSender {
        retentionManager = RetentionManager(context, period)
        return this
    }

    fun send(transaction: LoggerModel) {
        val transactionUri = create(transaction)
//        update(transaction, transactionUri)
    }

    private fun create(transaction: LoggerModel): Uri? {
        val values = LocalCupboard.getInstance().withEntity(
            LoggerModel::class.java
        ).toContentValues(transaction)
        val uri = context.contentResolver.insert(LoggerContentProvider.TRANSACTION_URI, values)
        transaction.id = java.lang.Long.valueOf(uri?.lastPathSegment)
        if (showNotification) {
            notificationHelper.show(transaction)
        }
        retentionManager.doMaintenance()
        return null
    }

    private fun update(transaction: LoggerModel, uri: Uri?): Int? {
        val values = LocalCupboard.getInstance().withEntity(
            LoggerModel::class.java
        ).toContentValues(transaction)
        val updated = uri?.let { context.contentResolver.update(it, values, null, null) }
        if (updated != null) {
            if (showNotification && updated > 0) {
                notificationHelper.show(transaction)
            }
        }
        return updated
    }

    companion object {
        private const val LOG_TAG = "Notification Logger Interceptor"
        private val DEFAULT_RETENTION = Period.ONE_WEEK
        private val UTF8 = Charset.forName("UTF-8")
    }

    /**
     * @param context The current Context.
     */
    init {
        this.context = context.applicationContext
        notificationHelper = NotificationHelper(this.context)
        showNotification = true
        retentionManager = RetentionManager(this.context, DEFAULT_RETENTION)
    }
}