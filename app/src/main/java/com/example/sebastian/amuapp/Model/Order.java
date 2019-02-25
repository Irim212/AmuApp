package com.example.sebastian.amuapp.Model;

public class Order {
    private String MenuId;
    private String MenuName;
    private String RestaurantId;
    private String Amount;
    private String Price;


    public Order(){
    }

    public Order(String menuId, String menuName, String restaurantId, String amount, String price) {
        MenuId = menuId;
        MenuName = menuName;
        RestaurantId = restaurantId;
        Amount = amount;
        Price = price;

    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
