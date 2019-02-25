package com.example.sebastian.amuapp;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sebastian.amuapp.Common.Common;
import com.example.sebastian.amuapp.Database.Database;
import com.example.sebastian.amuapp.Model.Menu;
import com.example.sebastian.amuapp.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodAdd extends AppCompatActivity {

    TextView foodName, foodPrice, foodDescription;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton buyButton;
    ElegantNumberButton foodAmount;

    String foodId="";

    FirebaseDatabase db;
    DatabaseReference foods;

    Menu currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add);

        db = FirebaseDatabase.getInstance();
        foods = db.getReference("Menu");

        buyButton = (FloatingActionButton) findViewById(R.id.buyButton);
        foodName = (TextView) findViewById(R.id.foodName);
        foodPrice = (TextView) findViewById(R.id.foodPrice);
        foodDescription = (TextView) findViewById(R.id.foodDescription);
        foodAmount = (ElegantNumberButton) findViewById(R.id.amountButton);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaps);

        if(getIntent() != null)
        {
            foodId = getIntent().getStringExtra("FoodId");
            if(!foodId.isEmpty() && foodId != null)
            {
                getFoodInfo(foodId);
            }
        }

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.currentUser != null)
                {
                    buyButton.setEnabled(true);
                    new Database(getBaseContext()).addToCart(new Order(
                            foodId, currentFood.getName(),
                            currentFood.getRestaurantId(),
                            foodAmount.getNumber(),
                            currentFood.getPrice()
                    ));
                    Toast.makeText(FoodAdd.this, "Dodano do koszyka", Toast.LENGTH_SHORT).show();
                }else{
                    buyButton.setEnabled(false);
                    Toast.makeText(FoodAdd.this, "Zaloguj się by dokonać zakupu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getFoodInfo(String foodId) {

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Menu.class);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                foodPrice.setText(currentFood.getPrice());
                foodName.setText(currentFood.getName());
                foodDescription.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
