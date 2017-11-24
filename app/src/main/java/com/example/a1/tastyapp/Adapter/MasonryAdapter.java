package com.example.a1.tastyapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.tastyapp.Item.Restaurant;
import com.example.a1.tastyapp.R;
import com.example.a1.tastyapp.Request.DownloadImageTask;
import com.example.a1.tastyapp.ResDetailActivity;

import java.util.ArrayList;

/**
 * Created by Suleiman on 26-07-2015.
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {
    int layout;
    private Context mContext;
    private ArrayList<Restaurant> mItems = new ArrayList<Restaurant>();

    public MasonryAdapter(Context context, ArrayList<Restaurant> items) {
        mContext = context;
        mItems = items;
        layout =R.layout.grid_item;
    }
    public MasonryAdapter(Context context, ArrayList<Restaurant> items, int layout) {
        mContext = context;
        mItems = items;
        this.layout=layout;
    }
    public void addItem(Restaurant item) {
        mItems.add(item);
        notifyDataSetChanged();
    }
    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        MasonryView masonryView = new MasonryView(layoutView);
        return masonryView;
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        new DownloadImageTask(holder.imageView)
                .execute(""+mItems.get(position).getPicture());
        //holder.imageView.setImageBitmap(mItems.get(position).getPicture());
        holder.nameTextView.setText(mItems.get(position).getName());
        holder.pointTextView.setText(""+mItems.get(position).getPoint());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MasonryView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView nameTextView;
        TextView pointTextView;
        public MasonryView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            pointTextView = (TextView) itemView.findViewById(R.id.point);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                //User user = users.get(position);
                // We can access the data within the views
                Intent i = new Intent(mContext, ResDetailActivity.class);
                i.putExtra("name", nameTextView.getText());
                mContext.startActivity(i);

                //Toast.makeText(mContext, nameTextView.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}