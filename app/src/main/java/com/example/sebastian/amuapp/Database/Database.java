package com.example.sebastian.amuapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.sebastian.amuapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DbName="AmuAppDB2.db";
    private static final int DbVersion=1;

    public Database(Context context) {
        super(context, DbName, null, DbVersion);
    }

    public List<Order> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"MenuName", "MenuId", "RestaurantId", "Amount", "Price"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor cursor = qb.query(db, sqlSelect,null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do{
                result.add(new Order(cursor.getString(cursor.getColumnIndex("MenuId"))
                        , cursor.getString(cursor.getColumnIndex("MenuName"))
                        , cursor.getString(cursor.getColumnIndex("RestaurantId"))
                        , cursor.getString(cursor.getColumnIndex("Amount"))
                        , cursor.getString(cursor.getColumnIndex("Price"))));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(MenuId, MenuName, RestaurantId, Amount, Price) VALUES('%s', '%s', '%s', '%s', '%s');",
                order.getMenuId(),
                order.getMenuName(),
                order.getRestaurantId(),
                order.getAmount(),
                order.getPrice());
        db.execSQL(query);
    }

    public void removeFromCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

}
