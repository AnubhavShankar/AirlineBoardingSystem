package com.professional.anubhavshankar.airlineboardingsystem;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.professional.anubhavshankar.airlineboardingsystem.data.BookingContact;

import java.util.ArrayList;
import java.util.Vector;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int GET_BOOKINGS = 1;
    ArrayList<String> names= new ArrayList<String>();
    ArrayList<String> ages= new ArrayList<String>();
    TextView selectedSeats;
    TableRow tr;
    Cursor data;
    ArrayList<Integer> bookedSeats,chosenSeats;
    ArrayList<String> SeatID;
    String TripID="";
    int seats;
    Button book;
    String selectedSeatStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i=getIntent();
        chosenSeats=new ArrayList<Integer>(4);
        SeatID= new ArrayList<String>(4);
        String sortOrder= BookingContact.BookingEntry.COLUMN_Age;
        data=getContentResolver().query(BookingContact.BookingEntry.CONTENT_URI,
                BookingContact.BookingEntry.FULL_PROJECTION,null,null,null);
        ResetBookedSeats(data);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        selectedSeats = (TextView) findViewById(R.id.selected_seats);

        if (null!=i) {
            names=i.getStringArrayListExtra("names");
            ages=i.getStringArrayListExtra("ages");
            TripID=i.getStringExtra("trip");
        }
        seats=names.size();
        String text="";

        setChosenSeats();
        for(int j=0;j<chosenSeats.size();j++){
            text=text+Integer.toString(chosenSeats.get(j))+",";
        }

        publishBooking();
        selectedSeats.setText("Selected Seats "+selectedSeatStr);
        book=(Button)findViewById(R.id.book);
        book.setOnClickListener(this);
    }
    public void ResetBookedSeats(Cursor data){
        bookedSeats=new ArrayList<Integer>(24);
        int seat;

        while(data.moveToNext()){
            seat=Integer.parseInt(data.getString(BookingContact.BookingEntry.COL_SEAT));
            bookedSeats.add(seat);
        }

    }
    public void setChosenSeats(){
        int remainingSeats= seats;
        chosenSeats= new ArrayList<Integer>(4);
        int couple=0;
        int single=0;
        if(remainingSeats%3==0){
            int index=findInMiddle(3);
            if(index!=-1) {
                chosenSeats.add(index);
                chosenSeats.add(1 + index);
                chosenSeats.add(2 + index);
                Log.d("ConfirmActivity", "Found index at: " + index);
            }
            else{
                couple=couple+1;
                single=single+1;
            }
        }
        if(remainingSeats%4==0){
            int index=findInMiddle(4);
            if(index!=-1) {
                chosenSeats.add(index);
                chosenSeats.add(1 + index);
                chosenSeats.add(2 + index);
                chosenSeats.add(3 + index);
                Log.d("ConfirmActivity", "Found index at: " + index);
            }
            else{
                couple=couple+2;
            }
        }
        remainingSeats=remainingSeats-chosenSeats.size();
        couple=couple+remainingSeats/2;
        for(int i=0;i<couple;i++){
            int index=findInSides();
            if(index!=-1){
                chosenSeats.add(index);
                chosenSeats.add(1 + index);
                Log.d("ConfirmActivity", "Found couple index at: " + index);
            }
            else{
                index=findInMiddle(2);
                if(index!=-1){
                    chosenSeats.add(index);
                    chosenSeats.add(1 + index);
                    Log.d("ConfirmActivity", "Found couple index at: " + index);
                }
                else {
                    single = single + 1;
                }
            }
        }
        remainingSeats=remainingSeats-chosenSeats.size();
        for(int i=0;i<remainingSeats;i++){
            int index=findSingleSeat();
            chosenSeats.add(index);
            Log.d("ConfirmActivity", "Found Single index at: " + index);
        }
    }
    public int findSingleSeat(){
        int i=1;
        while(i<=30) {
            if (bookedSeats.contains(i)||chosenSeats.contains(i)||i%10==3||i%10==8) {
                i++;
            }
            else{
                return i;
            }
        }
        return -1;

    }
    public int findInSides(){
        int i=1;
        int count=0;
        int index=-1;
        while(i<=30){
            if(i%10==3){
                i=i+6;
                continue;
            }
            if(i%10==8||i%10==0){
                i=i+1;
                continue;
            }
            if(bookedSeats.contains(i)|| chosenSeats.contains(i))
                i=i+1;
            else{
                index=i;
                count=count+1;
                if(bookedSeats.contains(++i) || chosenSeats.contains(i))
                {
                    ++i;
                    continue;
                }
                else{
                    return index;
                }

            }
        }
        return -1;
    }
    public int findInMiddle(int size){
        int i=4;
        int count=0;
        int index=4;

        while(i<=30){
            Log.d("ConfirmActivity","inside while of middle find. current I: "+i);
            if(i%10==8) {
                if(count>=size){
                    return index;
                }
                count=0;
                i = i + 6;
                continue;
            }
            if(bookedSeats.contains(i)||chosenSeats.contains(i))
            {
                if(count>=size)
                    return index;
                else {
                    count=0;
                    index = ++i;
                }

            }
            else {
                count++;
                i++;
            }

        }
        return -1;
    }
    public void publishBooking(){

        int[] RowIds={R.id.row1,R.id.row2,R.id.row3};
        int idx=-1;
        char col='A';
        SeatID= new ArrayList<String>(4);
        String seatIDs;
        TextView tv;
        StringBuilder sb= new StringBuilder();
        for(int i=1;i<=30;i++){
            if(i%10==1)
            {
                idx++;
                col='A';
                tr=(TableRow) findViewById(RowIds[idx]);
                tr.removeAllViews();
            }
            tv= new TextView(getBaseContext());
            tv.setTextColor(Color.BLACK);
            tv.setPadding(5,20,5,20);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            //aisle row..
            if(i%10==3 || i%10==8){
                tv.setText(" - ");
                tv.setBackgroundColor(Color.GRAY);
            }
            else{
                seatIDs=Integer.toString(idx+1)+Character.toString(col);
                tv.setText(seatIDs);
                if(bookedSeats.contains(i))
                    tv.setBackgroundColor(Color.RED);
                else if(chosenSeats.contains(i)){
                    tv.setBackgroundColor(Color.GREEN);
                    sb.append(seatIDs+",");
                    SeatID.add(seatIDs);
                }

                else
                    tv.setBackgroundColor(Color.WHITE);
            }
            tr.addView(tv);
            col++;
        }
        if(sb.length()>1)
            sb.deleteCharAt(sb.length()-1);
        selectedSeatStr=sb.toString();

    }

    @Override
    public void onClick(View v) {
        Vector<ContentValues> cvVector= new Vector<ContentValues>();
        ContentValues cv;
        for(int i=0;i< names.size();i++){
            cv= new ContentValues();
            cv.put(BookingContact.BookingEntry.COLUMN_SeatNumber,chosenSeats.get(i));
            cv.put(BookingContact.BookingEntry.COLUMN_TripID,TripID);
            cv.put(BookingContact.BookingEntry.COLUMN_Name,names.get(i));
            cv.put(BookingContact.BookingEntry.COLUMN_Age,ages.get(i));
            cv.put(BookingContact.BookingEntry.COLUMN_SEAT_ID,SeatID.get(i));
            cvVector.add(cv);
            Log.d("ConfirmActivity","CV made: "+cv.toString());
        }
        ContentValues[] cvArray= new ContentValues[names.size()];
        cvVector.toArray(cvArray);
        int rows=getContentResolver().bulkInsert(BookingContact.BookingEntry.CONTENT_URI,cvArray);
        Log.d("ConfirmActivity","Number of tickets booked: "+rows);
        Intent i= new Intent(ConfirmActivity.this,DisplayTicketsActivity.class);
        i.putExtra("trip",TripID);
        finish();
        startActivity(i);
    }
}
