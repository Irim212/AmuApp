package com.example.sebastian.amuapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class CustomListView extends ArrayAdapter<String> {

    private String[] restaurantName;
    private String[] restaurantDescription;
    private Integer[] imgid;
    private LatLng[] restaurantLL;
    private Activity context;
    private ImageButton mShopButton, mMapButton;

    public CustomListView(Activity context, String[] restaurantName, String[] restaurantDescription, Integer[] imgid, LatLng[] restaurantLL) {
        super(context, R.layout.listview_component, restaurantName);

        this.context = context;
        this.restaurantName = restaurantName;
        this.restaurantDescription = restaurantDescription;
        this.imgid = imgid;
        this.restaurantLL = restaurantLL;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
            ViewHolder viewHolder = null;
            if(r==null)
            {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.listview_component, null, true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) r.getTag();
            }

            viewHolder.mImageView1.setImageResource(imgid[position]);
            viewHolder.mTextView1.setText(restaurantName[position]);
            viewHolder.mTextView2.setText(restaurantDescription[position]);
            viewHolder.mShopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderActivity = new Intent(CustomListView.this.getContext(), OrderActivity.class);
                    context.startActivity(orderActivity);
                }
            });

            viewHolder.mMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.pathBetweenLatLngAndUser(restaurantLL[position]);
                }
            });

        return r;
    }

    class ViewHolder
    {
        TextView mTextView1;
        TextView mTextView2;
        ImageView mImageView1;
        ImageButton mMapButton;
        ImageButton mShopButton;
        ViewHolder(View view)
        {
            mTextView1 = (TextView) view.findViewById(R.id.mRestaurantName);
            mTextView2 = (TextView) view.findViewById(R.id.mRestaurantDescription);
            mImageView1 = (ImageView) view.findViewById(R.id.mImageView);
            mShopButton = (ImageButton) view.findViewById(R.id.mShopButton);
            mMapButton = (ImageButton) view.findViewById(R.id.mMapButton);
        }

    }


}
