package com.example.sebastian.amuapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sebastian.amuapp.Interface.ItemClickListener;
import com.example.sebastian.amuapp.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView foodNameTextView;
    public TextView foodDescTextView;
    public TextView foodPriceTextView;

    private ItemClickListener itemClickListener;


    public MenuViewHolder(View itemView)
    {
        super(itemView);

        foodNameTextView = (TextView) itemView.findViewById(R.id.foodNameTextView);
        foodDescTextView = (TextView) itemView.findViewById(R.id.foodDescTextView);
        foodPriceTextView = (TextView) itemView.findViewById(R.id.foodPriceTextView);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v ,getAdapterPosition(), false);
    }
}
