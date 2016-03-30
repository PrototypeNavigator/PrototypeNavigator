package se.jolo.prototypenavigator.utils;

import android.util.Log;

/**
 * Created by Holstad on 30/03/16.
 */
public class UrlBuilderMarkerImg {
    private static final StringBuilder stringBuilder = new StringBuilder();
    private static final String BASE_URL = "https://chart.googleapis.com/chart?chst=d_map_pin_letter_withshadow&chld=";
    private static final String COLOR = "|FFF|000";

    public UrlBuilderMarkerImg(){}

    public static String getMarkerUrl(int number){
        stringBuilder.delete(0,stringBuilder.length());
        stringBuilder.append(BASE_URL);
        stringBuilder.append(number);
        stringBuilder.append(COLOR);
        Log.d("Marker URL", stringBuilder.toString());
        return stringBuilder.toString();
    }
}
