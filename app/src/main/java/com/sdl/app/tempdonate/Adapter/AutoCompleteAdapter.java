package com.sdl.app.tempdonate.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by vishvanatarajan on 29/10/17.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> data;
    private final String server = "https://trackapi.nutritionix.com/v2/search/instant?query=";
    private final String apiID = "86a01a1b";
    private final String apiKey = "13069e9d543a801cf67638d9355b1c2c";

    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super (context, resource);
        this.data = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public String getItem (int position) {
        return data.get (position);
    }

    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering (CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    HttpURLConnection conn = null;
                    InputStream input = null;
                    try {
                        URL url = new URL(server + constraint.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        // Set Headers
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("x-app-id", apiID);
                        conn.setRequestProperty("x-app-key", apiKey);

                        input = conn.getInputStream();
                        InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                        BufferedReader buffer = new BufferedReader (reader, 8192);
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = buffer.readLine()) != null)
                        {
                            builder.append (line);
                        }
                        JSONObject terms = new JSONObject (builder.toString());
                        ArrayList<String> suggestions = new ArrayList<>();
                        JSONArray brandedList = terms.getJSONArray("branded");
                        for (int i=0; i < brandedList.length(); i++) {
                            String item = brandedList.getJSONObject(i).getString("food_name");
                            suggestions.add(item);
                        }
                        JSONArray commonList = terms.getJSONArray("common");
                        for (int ind = 0; ind < commonList.length(); ind++) {
                            String term = commonList.getJSONObject(ind).getString("food_name");
                            suggestions.add (term);
                        }
                        results.values = suggestions;
                        results.count = suggestions.size();
                        data = suggestions;
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch(Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();
                }
                else notifyDataSetInvalidated();
            }
        };
    }


}
