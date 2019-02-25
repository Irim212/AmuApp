package com.example.sebastian.amuapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sebastian.amuapp.Common.Common;
import com.example.sebastian.amuapp.Database.Database;
import com.example.sebastian.amuapp.Model.Order;
import com.example.sebastian.amuapp.Model.Request;
import com.example.sebastian.amuapp.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference requests;

    static TextView totalTextView;
    Button buyButton;
    String restaurantId;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        totalTextView = (TextView) findViewById(R.id.total);
        buyButton = (Button) findViewById(R.id.placeOrderButton);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        
        loadListFood();
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Wybierz adres");
        alertDialog.setMessage("Zostaw puste pole by wybrać domyślny adres");
        final EditText addressEditText = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        addressEditText.setLayoutParams(lp);
        alertDialog.setView(addressEditText);
        alertDialog.setIcon(R.drawable.cart12);
        alertDialog.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addressEditText.getText().toString().equals(""))
                {
                    Request request = new Request(
                            Common.currentUser.getPhoneNumber(),
                            Common.currentUser.getFirstName(),
                            Common.currentUser.getAddress(),
                            totalTextView.getText().toString(),
                            restaurantId,
                            cart
                    );
                    System.out.println(Common.currentUser.getAddress());
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                }else{
                    Request request = new Request(
                            Common.currentUser.getPhoneNumber(),
                            Common.currentUser.getFirstName(),
                            addressEditText.getText().toString(),
                            totalTextView.getText().toString(),
                            restaurantId,
                            cart
                    );
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                }
                totalTextView.setText("0");
                new Database(getBaseContext()).removeFromCart();
                finish();
            }
        });
        alertDialog.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);
        restaurantId = cart.get(0).getRestaurantId();

        double totalPrice = 0;
        for(Order order:cart)
        {
            String priceChanger = order.getPrice();
            String price = priceChanger.replace(",", ".");
            totalPrice += (Double.parseDouble(price))*(Double.parseDouble(order.getAmount()));
            Locale local = new Locale("pl", "PL");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
            totalTextView.setText(fmt.format(totalPrice));
        }
    }
}
