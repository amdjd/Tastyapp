package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.a1.tastyapp.Adapter.ResAdapter;
import com.example.a1.tastyapp.Adapter.SpacesItemDecoration;
import com.example.a1.tastyapp.Item.Restaurant;
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


public class QueryResData extends PostRequest {


    String layoutManager;
    ArrayList<Restaurant> restaurantItem;
    SpacesItemDecoration decoration;

    public QueryResData(Activity activity) {
        super(activity);
    }
    public QueryResData(Activity activity, String layoutManager){
        super(activity);
        this.layoutManager=layoutManager;
    }
    @Override
    protected void onPreExecute() {
        try {
            url = new URL(serverURLStr + "/query-res");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;

        restaurantItem = getArrayListFromJSONString(jsonString);
        ResAdapter adapter = new ResAdapter(activity, restaurantItem);
        RecyclerView mRecyclerView = (RecyclerView)activity.findViewById(R.id.masonry);
        if(layoutManager=="LinearLayoutManager") {

            //mRecyclerView.setNestedScrollingEnabled(false);
            LinearLayoutManager linearLayoutManagernew = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false){
                @Override public boolean canScrollHorizontally() { return false; }
            };
            mRecyclerView.setLayoutManager(linearLayoutManagernew);
            adapter = new ResAdapter(activity, restaurantItem, R.layout.mylist_item);
        }
        else
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(adapter);

        if(activity instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.geoTitle();
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
 /*       if(decoration==null) {
            decoration = new SpacesItemDecoration(16);
            mRecyclerView.addItemDecoration(decoration);
        }*/
        if(activity instanceof NavigateActivity) {
            NavigateActivity navigateActivity = (NavigateActivity) activity;
            navigateActivity.setRestaurantMarker(restaurantItem);
        }
        adapter.notifyDataSetChanged();
    }


    private static ArrayList<Restaurant> getArrayListFromJSONString(String jsonString) {
        ArrayList<Restaurant> output = new ArrayList();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                Restaurant restaurant = new Restaurant(jsonObject.getString("_id"),
                        jsonObject.getDouble("point"),
                        jsonObject.getString("name"),
                        new URL("http:/13.124.86.208:3000/"+jsonObject.getString("picture")),
                        jsonObject.getString("tel"),
                        jsonObject.getString("address"),
                        Double.parseDouble(jsonObject.getJSONObject("loc").getJSONArray("coordinates").get(0).toString()),
                        Double.parseDouble(jsonObject.getJSONObject("loc").getJSONArray("coordinates").get(1).toString()),
                        jsonObject.getString("businesshours"),
                        jsonObject.getString("type")
                );
                output.add(restaurant);
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

