package com.professional.anubhavshankar.airlineboardingsystem;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.professional.anubhavshankar.airlineboardingsystem.data.BookingContact;
import com.professional.anubhavshankar.airlineboardingsystem.data.BookingDetail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DisplayTicketsActivity extends AppCompatActivity {
    Cursor cursor;
    ByteArrayOutputStream os=null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu,menu);
         return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_send){
            if(null!=os) {
                Intent mShareIntent = new Intent();
                mShareIntent.setAction(Intent.ACTION_SEND);
                mShareIntent.setType("application/pdf");
                mShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Here is a PDF from PdfSend");
                mShareIntent.putExtra(
                        getClass().getPackage().getName() + "." + "SendPDF",
                        os.toByteArray());
                startActivity(mShareIntent);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tickets);
        Intent i=getIntent();
        LinearLayout ll=(LinearLayout)findViewById(R.id.activity_display_tickets);
        String tripID=i.getStringExtra("trip");
        Uri uri= BookingContact.BookingEntry.CONTENT_URI.buildUpon().appendPath(tripID).build();
        cursor=getContentResolver().query(uri, BookingContact.BookingEntry.FULL_PROJECTION,null,null,null);
        BookingDetail bd;
        TextView tv;


        // repaint the user's text into the page
        while(cursor.moveToNext()){
            bd= new BookingDetail(cursor.getString(BookingContact.BookingEntry.COL_TRIP_ID),
                    cursor.getString(BookingContact.BookingEntry.COL_NAME),
                    cursor.getString(BookingContact.BookingEntry.COL_AGE),
                    cursor.getString(BookingContact.BookingEntry.COL_SEAT_ID));
            tv= new TextView(getBaseContext());
            tv.setText(bd.toString());
            tv.setBackgroundColor(Color.WHITE);
            tv.setTextColor(Color.BLACK);
            tv.setAllCaps(true);
            tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_large));
            ll.addView(tv);
        }
        PdfDocument document= new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 300, 1).create();

        // create a new page from the PageInfo
        PdfDocument.Page page = document.startPage(pageInfo);
        View content=ll;
        content.draw(page.getCanvas());
        document.finishPage(page);
        os= new ByteArrayOutputStream();
        try {
            document.writeTo(os);
            document.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
