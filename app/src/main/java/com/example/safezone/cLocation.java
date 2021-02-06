package com.example.safezone;

public class cLocation {

    public String LocationName;
    public double Latitude , Longitude;
    public long Counter , PositiveCounter;
    public String ID_1;


    public cLocation(String LocationName , double latitude, double longitude, long Counter, String ID_1,long PositiveCounter ){

        this.LocationName = LocationName;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Counter = Counter;
        this.ID_1 = ID_1;
        this.PositiveCounter = PositiveCounter;

    }
}
