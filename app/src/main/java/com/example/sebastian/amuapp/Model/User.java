package com.example.sebastian.amuapp.Model;

public class User {

    private String FirstName;
    private String LastName;
    private String Email;
    private String City;
    private String Address;
    private String Password;

    public User(){

    }

    public User(String firstName, String lastName, String email, String city, String address, String password) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        City = city;
        Address = address;
        Password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
