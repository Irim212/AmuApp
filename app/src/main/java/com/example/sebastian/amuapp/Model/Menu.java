package com.example.sebastian.amuapp.Model;

public class Menu {
    private String foodName;
    private String foodDesc;
    private String price;
    private String restaurantId;

    public Menu(){

    }

    public Menu(String foodName, String foodDesc, String price, String restaurantId) {
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
