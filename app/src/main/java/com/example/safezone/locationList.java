package com.example.safezone;

public class locationList {
    String locationName , visitorsCounter , positiveVisitorsCounter;

    public locationList(String locationName, String visitorsCounter, String positiveVisitorsCounter) {
        this.locationName = locationName;
        this.visitorsCounter = visitorsCounter;
        this.positiveVisitorsCounter = positiveVisitorsCounter;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getVisitorsCounter() {
        return visitorsCounter;
    }

    public String getPositiveVisitorsCounter() {
        return positiveVisitorsCounter;
    }
}
