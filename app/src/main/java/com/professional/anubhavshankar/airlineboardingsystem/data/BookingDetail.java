package com.professional.anubhavshankar.airlineboardingsystem.data;

/**
 * Created by Anubhav Shankar on 10/16/2016.
 */

public class BookingDetail {
    String name;
    String age;
    String TripID;
    String SeatNumber;
    public BookingDetail(String tripID,String Name,String age,String SeatNumber){
        name=Name;
        this.age=age;
        this.TripID=tripID;
        this.SeatNumber=SeatNumber;
    }
    @Override
    public String toString() {
        String displayString="Trip ID       :  "+TripID+"\n"+
                             "Passenger Name:    "+name+"\n"+
                             "Age           :   "+age+"\n"+
                             "Seat Number   :   "+SeatNumber+"\n\n\n\n"+
                             "--------cut from here--------------------------";
        return displayString;
    }
}
