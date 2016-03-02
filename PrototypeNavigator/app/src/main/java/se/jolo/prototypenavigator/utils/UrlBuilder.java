package se.jolo.prototypenavigator.utils;

import android.location.Location;
import android.util.Log;

/**
 * Created by Holstad on 01/03/16.
 */
public class UrlBuilder {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=";
    private final String heading = "&fov=100&heading=235&pitch=10&";
    private final String API_KEY = "key=AIzaSyBxJe68IZK3SyPfibunetJFcI3_m91eqvw";
    private final float lon;
    private final float lat;


    public UrlBuilder(float lon, float lat){
        this.lat=lat;
        this.lon=lon;
    }

    public String getUrl(){
        stringBuilder.append(BASE_URL);
        stringBuilder.append(lat+",");
        stringBuilder.append(lon);
        stringBuilder.append(heading);
        stringBuilder.append(API_KEY);
        Log.d("url",stringBuilder.toString());
        return stringBuilder.toString();
    }

}
