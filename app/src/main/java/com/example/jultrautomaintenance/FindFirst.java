package com.example.jultrautomaintenance;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Coding Cafe on 7/18/2018.
 */

public class FindFirst extends AsyncTask<Object, String, String>
{
    private String googleplaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects)
    {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try
        {
            googleplaceData = downloadUrl.ReadTheURL(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googleplaceData;
    }


    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultsArray = parentObject.getJSONArray("results");
                JSONObject jsonObject = resultsArray.getJSONObject(0);
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String lat = locationObj.getString("lat");
                String lng = locationObj.getString("lng");

                JSONObject nameObj = resultsArray.getJSONObject(0);

                String name = nameObj.getString("name");

                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);

        } catch (JSONException e) {

        }

    }



}