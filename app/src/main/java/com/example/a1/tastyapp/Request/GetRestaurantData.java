package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.a1.tastyapp.Adapter.MasonryAdapter;
import com.example.a1.tastyapp.Adapter.SpacesItemDecoration;
import com.example.a1.tastyapp.Item.Restaurant;
import com.example.a1.tastyapp.R;

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
                        jsonObject.getDouble("point"),
                        jsonObject.getString("name"),
                        getBitmapFromString(jsonObject.getJSONObject("picture")),
                        jsonObject.getString("tel"),
                        jsonObject.getString("address"),
                        jsonObject.getDouble("latiude"),
                        jsonObject.getDouble("longitude"),
                        jsonObject.getString("businesshours"),
                        jsonObject.getString("businesshours")
                        );

                output.add(restaurant);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }


        return output;

    }

    private Bitmap getBitmapFromString(JSONObject obj) throws JSONException {
        Bitmap bitmap=null;
        byte[] tmp=new byte[obj.getJSONArray("data").length()];
        for(int i=0;i<obj.getJSONArray("data").length();i++){
            tmp[i]=(byte)(((int)obj.getJSONArray("data").get(i)) & 0xFF);
        }
        bitmap= BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        //Toast.makeText(activity,""+tmp[1],Toast.LENGTH_SHORT).show();
        return bitmap;
    }

}
