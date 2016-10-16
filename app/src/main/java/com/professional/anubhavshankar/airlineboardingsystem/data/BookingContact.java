package com.professional.anubhavshankar.airlineboardingsystem.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anubhav Shankar on 10/16/2016.
 */

public class BookingContact {
    public static final String CONTENT_AUTHORITY="com.professional.anubhavshankar.airlineboardingsystem.app";
    public static final Uri Base_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKING="booking";
    public static final class BookingEntry implements BaseColumns{
        public static final Uri CONTENT_URI=Base_CONTENT_URI.buildUpon().appendPath(PATH_BOOKING).build();
        public static final String CONTENT_TYPE="vnd.android.cursor.dir/"+CONTENT_AUTHORITY+"/"+PATH_BOOKING;
        public static final String CONTENT_ITEM_TYPE="vnd.android.cursor.item/"+CONTENT_AUTHORITY+"/"+PATH_BOOKING;
        public static final String TABLE_NAME="booking";
        public static final String COLUMN_SeatNumber="seat_number";
        public static final String COLUMN_TripID="trip_id";
        public static final String COLUMN_Name="name";
        public static final String COLUMN_Age="age";
        public static final String COLUMN_SEAT_ID="seat_id";

        public static String getTripIDFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
        public static final Uri buildbookingUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        public static final String[] FULL_PROJECTION={
                _ID,COLUMN_SeatNumber,COLUMN_TripID,COLUMN_Name,COLUMN_Age,COLUMN_SEAT_ID
        };
        public static final int COL_ID=0;
        public static final int COL_SEAT=1;
        public static final int COL_TRIP_ID=2;
        public static final int COL_NAME=3;
        public static final int COL_AGE=4;
        public static final int COL_SEAT_ID=5;
    };

}
