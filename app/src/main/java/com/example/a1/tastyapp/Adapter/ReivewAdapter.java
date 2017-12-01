package com.example.a1.tastyapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1.tastyapp.Item.Review;
import com.example.a1.tastyapp.R;
import com.example.a1.tastyapp.Request.DownloadImageTask;

import java.util.ArrayList;

/**
 * Created by 1 on 2017-11-29.
 */

public class ReivewAdapter extends RecyclerView.Adapter<ReivewAdapter.MasonryView> {
    int layout;
    private Context mContext;
    private ArrayList<Review> mItems = new ArrayList<Review>();

    public ReivewAdapter(Context context, ArrayList<Review> items) {
        mContext = context;
        mItems = items;
        layout = R.layout.grid_item;
    }
    public ReivewAdapter(Context context, ArrayList<Review> items, int layout) {
        mContext = context;
        mItems = items;
        this.layout=layout;
    }
    public void addItem(Review item) {
        mItems.add(item);
        notifyDataSetChanged();
    }
    @Override
    public ReivewAdapter.MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ReivewAdapter.MasonryView masonryView = new ReivewAdapter.MasonryView(layoutView);
        return masonryView;
    }

    @Override
    public void onBindViewHolder(ReivewAdapter.MasonryView holder, int position) {
        new DownloadImageTask(holder.imageView)
                .execute(""+mItems.get(position).getPicture());
        //holder.imageView.setImageBitmap(mItems.get(position).getPicture());
        holder.nameTextView.setText(mItems.get(position).getUser_id());
        holder.pointTextView.setText(""+mItems.get(position).getPoint());
        holder.memoTextView.setText(""+mItems.get(position).getMemo());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MasonryView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView nameTextView;
        TextView pointTextView;
        TextView memoTextView;
        public MasonryView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            pointTextView = (TextView) itemView.findViewById(R.id.point);
            memoTextView = (TextView) itemView.findViewById(R.id.memo);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
/*            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                //User user = users.get(position);
                // We can access the data within the views
                Intent i = new Intent(mContext, ResDetailActivity.class);
                i.putExtra("name", nameTextView.getText());
                mContext.startActivity(i);

                //Toast.makeText(mContext, nameTextView.getText(), Toast.LENGTH_SHORT).show();
            }*/
        }
    }

}
