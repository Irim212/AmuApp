package com.example.sebastian.amuapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

public class CustomListView extends ArrayAdapter<String> {

    private String[] restaurantName;
    private String[] restaurantDescription;
    private Integer[] imgid;
    private LatLng[] restaurantLL;
    private Activity context;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

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

        return r;
    }

    class ViewHolder
    {
        TextView mTextView1;
        TextView mTextView2;
        ImageView mImageView1;
        ViewHolder(View view)
        {
            mTextView1 = (TextView) view.findViewById(R.id.mRestaurantName);
            mTextView2 = (TextView) view.findViewById(R.id.mRestaurantDescription);
            mImageView1 = (ImageView) view.findViewById(R.id.mImageView);

        }


    }


}