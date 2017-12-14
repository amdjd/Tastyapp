package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.a1.tastyapp.Adapter.ReivewAdapter;
import com.example.a1.tastyapp.Adapter.SpacesItemDecoration;
import com.example.a1.tastyapp.Item.Review;
import com.example.a1.tastyapp.MainActivity;
import com.example.a1.tastyapp.NavigateActivity;
import com.example.a1.tastyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by 1 on 2017-11-29.
 */

public class PostReviewRes extends PostRequest{


    String layoutManager;
    ArrayList<Review> restaurantItem;
    SpacesItemDecoration decoration;

    public PostReviewRes(Activity activity) {
        super(activity);
    }
    public PostReviewRes(Activity activity, String layoutManager){
        super(activity);
        this.layoutManager=layoutManager;
    }
    @Override
    protected void onPreExecute() {
        try {
            url = new URL(serverURLStr + "/query-resview");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;
        restaurantItem = getArrayListFromJSONString(jsonString);

        ReivewAdapter adapter = new ReivewAdapter(activity, restaurantItem);
        RecyclerView mRecyclerView = (RecyclerView)activity.findViewById(R.id.masonry);
        LinearLayoutManager linearLayoutManagernew = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManagernew);
        adapter = new ReivewAdapter(activity, restaurantItem, R.layout.review_item);
        mRecyclerView.setAdapter(adapter);

        if(activity instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) activity;
            decoration=mainActivity.getDecoration();
            if(decoration==null) {
                decoration = new SpacesItemDecoration(16);
                mainActivity.setDecoration(decoration);
                mRecyclerView.addItemDecoration(decoration);
            }
        }
        if(activity instanceof NavigateActivity){
            NavigateActivity navigateActivity = (NavigateActivity) activity;
            decoration=navigateActivity.getDecoration();
            if(decoration==null) {
                decoration = new SpacesItemDecoration(16);
                navigateActivity.setDecoration(decoration);
                mRecyclerView.addItemDecoration(decoration);
            }
        }
        /*if(decoration==null) {
            decoration = new SpacesItemDecoration(16);
            mRecyclerView.addItemDecoration(decoration);
        }*/
        adapter.notifyDataSetChanged();
    }


    private static ArrayList<Review> getArrayListFromJSONString(String jsonString) {
        ArrayList<Review> output = new ArrayList();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                Review review = new Review(jsonObject.getString("_id"),
                        jsonObject.getString("user_id"),
                        jsonObject.getString("resname"),
                        jsonObject.getDouble("point"),
                        jsonObject.getString("memo"),
                        new URL("http:/13.114.103.74:3000/"+jsonObject.getString("picture")),
                        jsonObject.getString("date")
                );
                output.add(review);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return output;

    }
}
