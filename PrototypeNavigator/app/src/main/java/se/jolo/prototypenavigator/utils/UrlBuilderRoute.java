package se.jolo.prototypenavigator.utils;

import android.util.Log;


import com.mapbox.mapboxsdk.geometry.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Holstad on 10/03/16.
 */
public class UrlBuilderRoute {
    private static String TAG = "UrlBuilderRoute";
    private static String BASE_URL = "http://188.166.12.62/viaroute?instructions=true&";
    private static String LOCATION = "loc=";

    public UrlBuilderRoute(){}

    public static URL twoPoints(LatLng origin, LatLng destination){
        try {
            Log.d(TAG, BASE_URL + LOCATION + origin.getLatitude() + "," + origin.getLongitude() + "&" + LOCATION + destination.getLatitude() + "," + destination.getLongitude());
            return new URL(BASE_URL + LOCATION + origin.getLatitude() + "," + origin.getLongitude() + "&" + LOCATION + destination.getLatitude() + "," + destination.getLongitude()) ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL multiplePoints(ArrayList<LatLng> points){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_URL);
        //int i = 1;
        for(LatLng latLng: points){
            stringBuilder.append(LOCATION);
            stringBuilder.append(latLng.getLatitude()+",");
            stringBuilder.append(latLng.getLongitude()+"&");

            //Log.d(TAG,i+" ::::: "+latLng.getLatitude()+" "+latLng.getLongitude());
            //i++;
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        Log.d(TAG,stringBuilder.toString());
        try {
            return new URL(stringBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}