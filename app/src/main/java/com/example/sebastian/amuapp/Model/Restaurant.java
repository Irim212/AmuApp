package com.example.sebastian.amuapp.Model;

public class Restaurant {
    private String Name;
    private String Description;
    private String Image;
    private String LatLng;

    public Restaurant() {

    }

    public Restaurant(String name, String description, String image, String latLng) {
        Name = name;
        Description = description;
        Image = image;
        LatLng = latLng;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLatLng() {
        return LatLng;
    }

    public void setLatLng(String latLng) {
        LatLng = latLng;
    }
}
