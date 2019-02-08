package com.example.sebastian.amuapp.Model;

public class Restaurant {
    private String name;
    private String description;
    private String image;
    private String latLng;

    public Restaurant(){

    }

    public Restaurant(String name, String description, String image, String latLng) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }
}
