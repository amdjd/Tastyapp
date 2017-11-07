package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;

import com.example.a1.tastyapp.Adapter.MasonryAdapter;
import com.example.a1.tastyapp.R;
import com.example.a1.tastyapp.Item.Restaurant;
import com.example.a1.tastyapp.Adapter.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kwanwoo on 2017. 10. 17..
 */

public class GetRestaurantData extends GetRequest {
    public GetRestaurantData(Activity activity) {
        super(activity);
    }
    final static String serverURLStr="http://13.114.103.74:3000";
    @Override
    protected void onPreExecute() {
;
        try {
            url = new URL(serverURLStr+"/get-restaurant");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;

        ArrayList<Restaurant> ImageItem = getArrayListFromJSONString(jsonString);
        MasonryAdapter adapter = new MasonryAdapter(activity, ImageItem);
        RecyclerView mRecyclerView = (RecyclerView)activity.findViewById(R.id.masonry_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);

    }

    protected ArrayList<Restaurant> getArrayListFromJSONString(String jsonString) {
        ArrayList<Restaurant> output = new ArrayList();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                Restaurant restaurant = new Restaurant(jsonObject.getString("_id"),
                        jsonObject.getString("user_id"),
                        jsonObject.getString("email"),
                        getBitmapFromString(jsonObject.getString("password")));

                output.add(restaurant);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        return output;
    }

    private Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
