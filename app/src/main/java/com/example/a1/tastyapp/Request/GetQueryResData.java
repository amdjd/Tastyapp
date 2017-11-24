package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a1.tastyapp.Adapter.ListViewAdapter;
import com.example.a1.tastyapp.Item.Restaurant;
import com.example.a1.tastyapp.MainActivity;
import com.example.a1.tastyapp.R;
import com.example.a1.tastyapp.ResDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 1 on 2017-11-22.
 */


public class GetQueryResData extends GetRequest {
    String layoutManager;

    ListView list;
    ListViewAdapter adapter;
    ArrayList<Restaurant> restaurantsList;

    public GetQueryResData(Activity activity) {
        super(activity);
    }

    @Override
    protected void onPostExecute(String jsonString) {
        if (jsonString == null)
            return;

        restaurantsList = getArrayListFromJSONString(jsonString);

        list = (ListView) activity.findViewById(R.id.listview);
        list.setVisibility(View.INVISIBLE);
        adapter = new ListViewAdapter(activity, restaurantsList);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Restaurant item = (Restaurant) parent.getItemAtPosition(position) ;

                Intent i = new Intent(activity, ResDetailActivity.class);
                i.putExtra("name", item.getName());
                activity.startActivity(i);
            }
        }) ;

        SearchView mSearchView;
        mSearchView = (SearchView) ((MainActivity) activity).getToolbar().getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                list.setVisibility(View.VISIBLE);
                String text = s;
                adapter.filter(text);
                if(s.isEmpty())
                    list.setVisibility(View.INVISIBLE);
                return false;
            }

        });

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
                        new URL("http:/13.114.103.74/"+jsonObject.getString("picture")),
                        jsonObject.getString("tel"),
                        jsonObject.getString("address"),
                        Double.parseDouble(jsonObject.getJSONObject("loc").getJSONArray("coordinates").get(0).toString()),
                        Double.parseDouble(jsonObject.getJSONObject("loc").getJSONArray("coordinates").get(1).toString()),
                        //jsonObject.getDouble("longitude"),
                        jsonObject.getString("businesshours"),
                        jsonObject.getString("businesshours")
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
