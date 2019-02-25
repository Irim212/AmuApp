package com.example.sebastian.amuapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sebastian.amuapp.Common.Common;
import com.example.sebastian.amuapp.Database.Database;
import com.example.sebastian.amuapp.Model.Menu;
import com.example.sebastian.amuapp.Interface.ItemClickListener;
import com.example.sebastian.amuapp.Model.Request;
import com.example.sebastian.amuapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Menu, MenuViewHolder> adapter;
    String restaurantId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOrder);
        setSupportActionBar(toolbar);

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        if(getIntent() != null)
        {
            restaurantId = getIntent().getStringExtra("RestaurantId");

            if(!restaurantId.isEmpty())
            {
                loadMenu(restaurantId);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ordertoolbarmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId())
        {
            case R.id.action_set:
                if(Common.currentUser != null)
                {
                    Intent cartIntent = new Intent(this, Cart.class);
                    this.startActivity(cartIntent);
                    break;
                }else{
                    Toast.makeText(this, "Zaloguj się by przejść do koszyka", Toast.LENGTH_SHORT).show();
                    break;
                }
            default:
                break;
        }
        return true;
    }

    private void loadMenu(String restaurantId) {
        Query mMenu = FirebaseDatabase.getInstance().getReference("/Menu").orderByChild("RestaurantId").equalTo(restaurantId);
        FirebaseRecyclerOptions<Menu> options =
                new FirebaseRecyclerOptions.Builder<Menu>()
                        .setQuery(mMenu, Menu.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Menu, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item_component, viewGroup, false);
                return new MenuViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Menu model) {
                holder.foodNameTextView.setText(model.getName());
                holder.foodDescTextView.setText(model.getDescription());
                holder.foodPriceTextView.setText(model.getPrice());
                final Menu clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(OrderActivity.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodDetail = new Intent(OrderActivity.this, FoodAdd.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderActivity.this);
            alertDialog.setTitle("Czy chcesz usunąć koszyk?");
            alertDialog.setMessage("Wybierz tak jeśli chcesz zrezygnować z zamówienia");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            alertDialog.setPositiveButton("Opuść", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Database(getBaseContext()).removeFromCart();
                    if(!Cart.totalTextView.getText().equals(null))
                    {
                        Cart.totalTextView.setText("0");
                    }
                    finish();
                }
            });
            alertDialog.setNegativeButton("Zostań", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
    }
}
