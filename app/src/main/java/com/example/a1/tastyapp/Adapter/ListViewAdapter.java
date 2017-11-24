package com.example.a1.tastyapp.Adapter;

/**
 * Created by 1 on 2017-11-22.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a1.tastyapp.Item.Restaurant;
import com.example.a1.tastyapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<Restaurant> RestaurantNamesList = null;
    private ArrayList<Restaurant> arraylist;

    public ListViewAdapter(Context context, List<Restaurant> RestaurantNamesList) {
        mContext = context;
        this.RestaurantNamesList = RestaurantNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Restaurant>();
        this.arraylist.addAll(RestaurantNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return RestaurantNamesList.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return RestaurantNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(RestaurantNamesList.get(position).getName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        RestaurantNamesList.clear();
        if (charText.length() == 0) {
            RestaurantNamesList.addAll(arraylist);
        } else {
            for (Restaurant wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    RestaurantNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}