package com.professional.anubhavshankar.airlineboardingsystem;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.professional.anubhavshankar.airlineboardingsystem.data.BookingContact;

import java.util.ArrayList;
import java.util.Arrays;

public class BookingDetails extends AppCompatActivity implements Spinner.OnItemSelectedListener,Button.OnClickListener {
    int numberOfTravellers=1;
    int prevNumTravellers=1;
    LinearLayout sv;
    Cursor cursor;
    final int MAX_SEATS=24;
    ArrayList<String> names= new ArrayList<String>(24);
    ArrayList<String> ages= new ArrayList<String>(24);
    ArrayList<TravellerInfoView> tiv= new ArrayList<TravellerInfoView>();
    Spinner sp;
    String TripID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        cursor=getContentResolver().query(BookingContact.BookingEntry.CONTENT_URI,null,null,null,null);
        int booked=cursor.getCount();
        TripID="6E "+Integer.toString(booked+1);
        int available=MAX_SEATS-booked;
        Integer[] values=new Integer[available];
        for(int i=0;i<available;i++)
            values[i]=i+1;
        ArrayAdapter<Integer> adapter= new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,values);
        sp=(Spinner)findViewById(R.id.num_traveller);
        sp.setAdapter(adapter);
        sp.setSelection(0);
        sp.setOnItemSelectedListener(this);
        sv=(LinearLayout)findViewById(R.id.trvlr_detalis);

        TravellerInfoView temp;
        temp= new TravellerInfoView(getBaseContext());
        tiv.add(temp);
        sv.addView(temp);

        Button proceed= (Button)findViewById(R.id.btnProceed);
        proceed.setOnClickListener(this);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        numberOfTravellers=position+1;
        TravellerInfoView temp;
        if(numberOfTravellers<prevNumTravellers){
            for(int i=numberOfTravellers;i<prevNumTravellers;i++) {
                if(!tiv.isEmpty()) {
                    int index=tiv.size() - 1;
                    sv.removeView(tiv.get(index));
                    tiv.remove(index);
                }
            }
        }
        else
        {
            for(int i=prevNumTravellers;i<numberOfTravellers;i++){
                temp= new TravellerInfoView(getBaseContext());
                tiv.add(temp);
                sv.addView(temp);
            }
        }
        prevNumTravellers=numberOfTravellers;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {

        boolean EmptyFields=false;
        for(int i=0;i<tiv.size();i++){
            String temp=tiv.get(i).trvlrName.getText().toString();
            Log.d("BookingDetails","name: "+temp);
            String temp2=tiv.get(i).trvlrAge.getText().toString();
            Log.d("BookingDetails","age: "+temp2);
            if(temp.trim().equals("") || temp2.trim().equals("")){
                Toast.makeText( getBaseContext(), "Fill all details", Toast.LENGTH_SHORT).show();
                EmptyFields=true;
                break;
            }
            else{
                names.add(temp);
                ages.add(temp2);
            }
        }
        if (!EmptyFields){
            Log.d("BookingDetails","sending intent...");
            finish();
            //Toast.makeText(getBaseContext(), Integer.toString(names.size()),Toast.LENGTH_LONG).show();
            Intent i = new Intent(BookingDetails.this,ConfirmActivity.class);
            i.putStringArrayListExtra("names",names);
            i.putStringArrayListExtra("ages",ages);
            i.putExtra("trip",TripID);
            startActivity(i);
        }
    }
    public void screenReset(){
        sv.removeAllViews();
        tiv.clear();
        TravellerInfoView temp;
        temp= new TravellerInfoView(getBaseContext());
        tiv.add(temp);
        sv.addView(temp);
        sp.setSelection(0);
    }
}
