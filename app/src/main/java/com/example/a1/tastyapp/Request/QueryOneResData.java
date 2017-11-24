package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1.tastyapp.Item.Restaurant;
import com.example.a1.tastyapp.ResDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by 1 on 2017-11-23.
 */

public class QueryOneResData extends PostRequest{
    public QueryOneResData(Activity activity) {
        super(activity);
    }

    protected void onPreExecute() {
        try {
            url = new URL(serverURLStr + "/query-oneres");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;
        ArrayList<Restaurant> restaurants = getArrayListFromJSONString(jsonString);
        if(!restaurants.isEmpty()) {
            Restaurant restaurant = restaurants.get(0);

            ResDetailActivity resDetailActivity = (ResDetailActivity) activity;
            ImageView imageView = resDetailActivity.getImageView();
            new DownloadImageTask(imageView)
                    .execute(""+restaurant.getPicture());
            ArrayList<TextView> textViews = resDetailActivity.getTextView();
            textViews.get(0).setText(restaurant.getName());
            textViews.get(1).setText("" + restaurant.getPoint());
            textViews.get(2).setText(restaurant.getAdress());
            textViews.get(3).setText(restaurant.getTel());
            textViews.get(4).setText(restaurant.getBusinesshours());

        }else
            Toast.makeText(activity, "err", Toast.LENGTH_SHORT).show();
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
                        new URL("http:/13.114.103.74/"+jsonObject.getString("picture")),
                        jsonObject.getString("tel"),
                        jsonObject.getString("address"),
                        Double.parseDouble(jsonObject.getJSONObject("loc").getJSONArray("coordinates").get(0).toString()),
                        Double.parseDouble(jsonObject.getJSONObject("loc").getJSONArray("coordinates").get(1).toString()),
                        jsonObject.getString("businesshours"),
                        jsonObject.getString("menu")
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
