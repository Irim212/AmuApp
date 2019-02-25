package com.example.sebastian.amuapp.ViewHolder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sebastian.amuapp.Interface.ItemClickListener;
import com.example.sebastian.amuapp.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView restuarantNameTextView;
    public TextView restaurantDescTextView;
    public ImageView restaurantImageView;
    public ImageButton shopButton;
    public ImageButton pathButton;

    private ItemClickListener itemClickListener;

    public RestaurantViewHolder(View itemView) {
        super(itemView);

        restuarantNameTextView = (TextView) itemView.findViewById(R.id.mRestaurantName);
        restaurantDescTextView = (TextView) itemView.findViewById(R.id.mRestaurantDescription);
        restaurantImageView = (ImageView) itemView.findViewById(R.id.mImageView);
        shopButton = (ImageButton) itemView.findViewById(R.id.mShopButton);
        pathButton = (ImageButton) itemView.findViewById(R.id.mMapButton);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
