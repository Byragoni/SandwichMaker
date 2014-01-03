package com.android.sandwichmaker.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.sandwichmaker.provider.SandwichMakerDBHelper.Columns;
import com.android.sandwichmaker.provider.SandwichMakerDBHelper.Tables;

public class SandwichMakerProvider extends ContentProvider {

    private Context mContext;
    private SandwichMakerDBHelper mDbHelper;
    private SQLiteDatabase mDb;
    private ContentResolver mResolver;

    private static final int DATA_MOMENT = 1;
    private static final int DATA_ACTION = 2;

    private static final int INFO_MOMENT = 3;
    private static final int INFO_ACTION = 4;
    private static final int INFO_LOCATION = 5;

    private static final UriMatcher uriMatcher;
    private static final String TAG = "SandwichMakerProvider";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(SandwichMakerDBHelper.PHI_AUTHORITY, Tables.DATA_TABLE_MOMENTS, DATA_MOMENT);
        uriMatcher.addURI(SandwichMakerDBHelper.PHI_AUTHORITY, Tables.DATA_TABLE_ACTIONS, DATA_ACTION);

        uriMatcher.addURI(SandwichMakerDBHelper.PHI_AUTHORITY, Tables.INFO_TABLE_MOMENTS, INFO_MOMENT);
        uriMatcher.addURI(SandwichMakerDBHelper.PHI_AUTHORITY, Tables.INFO_TABLE_ACTIONS, INFO_ACTION);
        uriMatcher.addURI(SandwichMakerDBHelper.PHI_AUTHORITY, Tables.INFO_TABLE_LOCATION, INFO_LOCATION);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowId;
        String id;
        Log.d(TAG, "****inside delete***" + uri);

        switch (uriMatcher.match(uri)) {
            case DATA_MOMENT:
                id = selection;

                rowId = mDb.delete(Tables.DATA_TABLE_MOMENTS, Columns.COLUMN_MOMENT_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ")" : ""), selectionArgs);
                break;
            case DATA_ACTION:

                id = selection;
                rowId = mDb.delete(Tables.DATA_TABLE_ACTIONS, Columns.COLUMN_ACTION_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ")" : ""), selectionArgs);
                break;

            case INFO_MOMENT:
                id = selection;

                rowId = mDb.delete(Tables.INFO_TABLE_MOMENTS, Columns.COLUMN_MOMENT_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ")" : ""), selectionArgs);
                break;
            case INFO_ACTION:
                Log.d(TAG, "****inside EMERGENCY_EVENTS delete***" + uri);
                id = selection;
                rowId = mDb.delete(Tables.INFO_TABLE_ACTIONS, Columns.COLUMN_ACTION_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ")" : ""), selectionArgs);
                break;
            case INFO_LOCATION:
                Log.d(TAG, "****inside EMERGENCY_EVENTS delete***" + uri);
                id = selection;
                rowId = mDb.delete(Tables.INFO_TABLE_LOCATION, Columns.COLUMN_LOC_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND ("
                        + selection + ")" : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        mResolver.notifyChange(uri, null);

        return rowId;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {

        // get database to insert records
        long dataactionsRowId = 0;
        long datamomentsRowId = 0;
        long infoactionsRowId = 0;
        long infomomentsRowId = 0;
        long infolocationsRowId =0;

        Log.i("******inside Insert**********", "" + uri);

        // insert record in user table and get the row number of recently
        // inserted record

        switch (uriMatcher.match(uri)) {
            case DATA_MOMENT:

                datamomentsRowId = mDb.insert(Tables.DATA_TABLE_MOMENTS, "", contentvalues);
                break;
            case DATA_ACTION:

                dataactionsRowId = mDb.insert(Tables.DATA_TABLE_ACTIONS, "", contentvalues);
                break;

            case INFO_MOMENT:

                infomomentsRowId = mDb.insert(Tables.INFO_TABLE_MOMENTS, "", contentvalues);
                break;
            case INFO_ACTION:

                infoactionsRowId = mDb.insert(Tables.INFO_TABLE_ACTIONS, "", contentvalues);
                break;
            case INFO_LOCATION:

                infolocationsRowId = mDb.insert(Tables.INFO_TABLE_LOCATION, "", contentvalues);
                break;

            default:
                Log.e("Unknown URI ", "" + uri);
                break;
        }

        if (datamomentsRowId > 0) {
            Uri rowUri = ContentUris.appendId(SandwichMakerDBHelper.PHI_DATA_MOMENTS_CONTENT_URI.buildUpon(), datamomentsRowId).build();
            mResolver.notifyChange(rowUri, null);

            return rowUri;
        } else if (dataactionsRowId > 0) {
            Uri rowUri = ContentUris.appendId(SandwichMakerDBHelper.PHI_DATA_ACTIONS_CONTENT_URI.buildUpon(), dataactionsRowId).build();
            mResolver.notifyChange(rowUri, null);

            return rowUri;
        }
        else if (infoactionsRowId > 0) {
            Uri rowUri = ContentUris.appendId(SandwichMakerDBHelper.PHI_INFO_ACTIONS_CONTENT_URI.buildUpon(), dataactionsRowId).build();
            mResolver.notifyChange(rowUri, null);

            return rowUri;
        }
        else if (infomomentsRowId > 0) {
            Uri rowUri = ContentUris.appendId(SandwichMakerDBHelper.PHI_INFO_MOMENTS_CONTENT_URI.buildUpon(), infomomentsRowId).build();
            mResolver.notifyChange(rowUri, null);

            return rowUri;
        }
        else if (infolocationsRowId > 0) {
            Uri rowUri = ContentUris.appendId(SandwichMakerDBHelper.PHI_INFO_LOCATIONS_CONTENT_URI.buildUpon(), infolocationsRowId).build();
            mResolver.notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to create entry into " + uri);
    }

    @Override
    public boolean onCreate() {
        try {
            mContext = getContext();
            mDbHelper = new SandwichMakerDBHelper(mContext);
            mDb = mDbHelper.getWritableDatabase();
            mResolver = mContext.getContentResolver();
            return true;
        } catch (RuntimeException e) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Cannot start provider", e);
            }
            return false;
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Log.i(TAG, "URI : " + uri);

        switch (uriMatcher.match(uri)) {
            case DATA_MOMENT:
                qb.setTables(Tables.DATA_TABLE_MOMENTS);

                break;
            case DATA_ACTION:
                qb.setTables(Tables.DATA_TABLE_ACTIONS);

                break;
            case INFO_MOMENT:
                qb.setTables(Tables.INFO_TABLE_MOMENTS);

                break;
            case INFO_ACTION:
                qb.setTables(Tables.INFO_TABLE_ACTIONS);

                break;
            case INFO_LOCATION:
                qb.setTables(Tables.INFO_TABLE_LOCATION);

                break;

            default:
                Log.e(TAG, "Unknown URI : " + uri);
                break;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(mResolver, uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
                      String[] whereArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case DATA_MOMENT:
                count = db.update(Tables.DATA_TABLE_MOMENTS, values, where, whereArgs);
                break;
            case DATA_ACTION:
                count = db.update(Tables.DATA_TABLE_ACTIONS, values, where, whereArgs);
                break;
            case INFO_MOMENT:
                count = db.update(Tables.INFO_TABLE_MOMENTS, values, where, whereArgs);
                break;
            case INFO_ACTION:
                count = db.update(Tables.INFO_TABLE_ACTIONS, values, where, whereArgs);
                break;
            case INFO_LOCATION:
                count = db.update(Tables.INFO_TABLE_LOCATION, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
     getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public void shutdown() {
        if (mDbHelper != null) {
            mDbHelper.close();
            mDbHelper = null;
            mDb = null;
        }
    }


}
