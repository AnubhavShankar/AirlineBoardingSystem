package com.professional.anubhavshankar.airlineboardingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Anubhav Shankar on 10/15/2016.
 */

public class TravellerInfoView extends LinearLayout {
    public EditText trvlrName;
    public EditText trvlrAge;
    public TravellerInfoView(Context context) {
        super(context);
        String service= Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li= (LayoutInflater)getContext().getSystemService(service);
        li.inflate(R.layout.traveller_entry,this,true);
        trvlrName=(EditText)findViewById(R.id.psgrName);
        trvlrAge=(EditText)findViewById(R.id.psgrAge);
    }
}
