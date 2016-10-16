package com.professional.anubhavshankar.airlineboardingsystem.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Anubhav Shankar on 10/16/2016.
 */

public class BookingProvider extends ContentProvider {
    private static final String LOG_TAG=BookingProvider.class.getSimpleName();
    private static final int BOOKING=100;
    private static final int BOOKING_WITH_TRIP_ID=101;
    private static UriMatcher sUriMatcher=buildUriMatcher();
    private BookingDbHelper mOpentHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=BookingContact.CONTENT_AUTHORITY;
        matcher.addURI(authority,BookingContact.PATH_BOOKING,BOOKING);
        matcher.addURI(authority,BookingContact.PATH_BOOKING+"/*",BOOKING_WITH_TRIP_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpentHelper= new BookingDbHelper(getContext());
        return true;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match=sUriMatcher.match(uri);
        Cursor retCursor=null;
        switch (match){
            case BOOKING:{
                retCursor=mOpentHelper.getReadableDatabase().query(
                        BookingContact.BookingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                Log.d(LOG_TAG,"BOOKING PROVIDER Invoked successfully");
                break;
            }
            case BOOKING_WITH_TRIP_ID:{
                retCursor=getBookingByTripID(uri,projection,sortOrder);
                break;
            }
            default:
                Log.d(LOG_TAG,"BOOKING URI Unknown: "+uri);
                throw new UnsupportedOperationException("Unknown URI: "+uri.toString());
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return BookingContact.BookingEntry.CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match=sUriMatcher.match(uri);
        final SQLiteDatabase db=mOpentHelper.getWritableDatabase();
        Uri returnUri;
        switch (match){
            case BOOKING: {
                long _id = db.insert(BookingContact.BookingEntry.TABLE_NAME,
                        null,
                        values);
                if (_id > 0)
                    returnUri = BookingContact.BookingEntry.buildbookingUri(_id);
                else
                    throw new SQLException("Failed to insert Row into the database");
                break;
            }
            default: throw new UnsupportedOperationException("URI NOT RECOGNIZED: "+uri.toString());

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    public  Cursor getBookingByTripID(Uri uri,String[] projection,String sortOrder){
        String tripID= BookingContact.BookingEntry.getTripIDFromUri(uri);
        String[] selectionArgs =new String[] {tripID};
        Cursor retCursor=null;
        retCursor=mOpentHelper.getReadableDatabase().query(
                BookingContact.BookingEntry.TABLE_NAME,
                projection,
                BookingContact.BookingEntry.COLUMN_TripID+" = ? ",
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return retCursor;

    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db=mOpentHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        switch (match) {
            case BOOKING:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BookingContact.BookingEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return  returnCount;
            default:
                return super.bulkInsert(uri, values);
        }

    }
}
