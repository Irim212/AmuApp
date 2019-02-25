package com.example.sebastian.amuapp.Model;

public class Menu {
    private String Name;
    private String Description;
    private String Price;
    private String RestaurantId;

    public Menu() {

    }

    public Menu(String name, String description, String price, String restaurantId) {
        Name = name;
        Description = description;
        Price = price;
        RestaurantId = restaurantId;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }
}
