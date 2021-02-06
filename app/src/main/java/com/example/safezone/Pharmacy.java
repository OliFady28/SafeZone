package com.example.safezone;

import java.io.Serializable;

public class Pharmacy  implements Serializable {
    private String pharmacyName ;
    private int pharmacyDeliveryTime;
    private int pharmacyImg;
    private String hotline ;

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public Pharmacy(String pharmacyName, int pharmacyDeliveryTime, int pharmacyImg, String hotline) {
        this.pharmacyName = pharmacyName;
        this.pharmacyDeliveryTime = pharmacyDeliveryTime;
        this.pharmacyImg = pharmacyImg;
        this.hotline=hotline;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public int getPharmacyDeliveryTime() {
        return pharmacyDeliveryTime;
    }

    public void setPharmacyDeliveryTime(int pharmacyDeliveryTime) {
        this.pharmacyDeliveryTime = pharmacyDeliveryTime;
    }

    public int getPharmacyImg() {
        return pharmacyImg;
    }

    public void setPharmacyImg(int pharmacyImg) {
        this.pharmacyImg = pharmacyImg;
    }
}
