package com.sedo.notificationlogger.data.models;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoggerContentProvider extends ContentProvider {

    public static Uri TRANSACTION_URI;

    private static final int TRANSACTION = 0;
    private static final int TRANSACTIONS = 1;
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    private LoggerDbOpenHelper databaseHelper;

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        TRANSACTION_URI = Uri.parse("content://" + info.authority + "/transaction");
        matcher.addURI(info.authority, "transaction/#", TRANSACTION);
        matcher.addURI(info.authority, "transaction", TRANSACTIONS);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new LoggerDbOpenHelper(getContext());
        return true;
    }

    @Override
    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (matcher.match(uri)) {
            case TRANSACTIONS:
                cursor = LocalCupboard.getInstance().withDatabase(db).query(LoggerModel.class).
                        withProjection(projection).
                        withSelection(selection, selectionArgs).
                        orderBy(sortOrder).
                        getCursor();
                break;
            case TRANSACTION:
                cursor = LocalCupboard.getInstance().withDatabase(db).query(LoggerModel.class).
                        byId(ContentUris.parseId(uri)).
                        getCursor();
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (matcher.match(uri)) {
            case TRANSACTIONS:
                long id = db.insert(LocalCupboard.getInstance().getTable(LoggerModel.class), null, contentValues);
                if (id > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(TRANSACTION_URI, id);
                }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result = 0;
        switch (matcher.match(uri)) {
            case TRANSACTIONS:
                result = db.delete(LocalCupboard.getInstance().getTable(LoggerModel.class), selection, selectionArgs);
                break;
            case TRANSACTION:
                result = db.delete(LocalCupboard.getInstance().getTable(LoggerModel.class),
                        "_id = ?", new String[]{ uri.getPathSegments().get(1) });
                break;
        }
        if (result > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result = 0;
        switch (matcher.match(uri)) {
            case TRANSACTIONS:
                result = db.update(LocalCupboard.getInstance().getTable(LoggerModel.class), contentValues, selection, selectionArgs);
                break;
            case TRANSACTION:
                result = db.update(LocalCupboard.getInstance().getTable(LoggerModel.class), contentValues,
                        "_id = ?", new String[]{ uri.getPathSegments().get(1) });
                break;
        }
        if (result > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return result;
    }
}