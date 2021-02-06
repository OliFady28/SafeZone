package com.example.safezone;

public class User {

    public int  age;

    public String firstName , lastName, phoneNumber, password , status , locationName_0;

    public long locationCounter;


    public User(int age , String firstName, String lastName, String phoneNumber, String password , String status) {
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = status;
        this.locationName_0 = "null";
        this.locationCounter = 0;
    }

}


