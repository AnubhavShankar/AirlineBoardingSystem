package com.professional.anubhavshankar.airlineboardingsystem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anubhav Shankar on 10/16/2016.
 */

public class BookingDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=2;
    public static final String DATABASE_NAME="flight.db";
    public BookingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BOOKING_TABLE="CREATE TABLE "+ BookingContact.BookingEntry.TABLE_NAME+"("+
                BookingContact.BookingEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                BookingContact.BookingEntry.COLUMN_SeatNumber+" TEXT NOT NULL,"+
                BookingContact.BookingEntry.COLUMN_TripID+" TEXT NOT NULL,"+
                BookingContact.BookingEntry.COLUMN_Name+" TEXT NOT NULL,"+
                BookingContact.BookingEntry.COLUMN_Age+" TEXT NOT NULL,"+
                BookingContact.BookingEntry.COLUMN_SEAT_ID+" TEXT NOT NULL"+
                ");";
        db.execSQL(SQL_CREATE_BOOKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+ BookingContact.BookingEntry.TABLE_NAME);
        onCreate(db);

    }
}
