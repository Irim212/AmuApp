package com.example.sebastian.amuapp.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.sebastian.amuapp.Interface.ItemClickListener;
import com.example.sebastian.amuapp.Model.Order;
import com.example.sebastian.amuapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cartItemNameTextView;
    public TextView cartItemPriceTextView;
    public ImageView cartItemAmountImageView;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartItemNameTextView = (TextView) itemView.findViewById(R.id.cartItemNameTextView);
        cartItemPriceTextView = (TextView) itemView.findViewById(R.id.cartItemPriceTextView);
        cartItemAmountImageView = (ImageView) itemView.findViewById(R.id.cartItemAmountImageView);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter() {
    }

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, viewGroup, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(i).getAmount(), Color.RED);
        cartViewHolder.cartItemAmountImageView.setImageDrawable(drawable);

        Locale local = new Locale("pl", "PL");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
        String priceChanger = listData.get(i).getPrice();
        String changedPrice = priceChanger.replace(",", ".");
        double price = (Double.parseDouble(changedPrice))*(Double.parseDouble(listData.get(i).getAmount()));
        cartViewHolder.cartItemPriceTextView.setText(fmt.format(price));
        cartViewHolder.cartItemNameTextView.setText(listData.get(i).getMenuName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
