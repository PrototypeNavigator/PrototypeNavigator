package se.jolo.prototypenavigator.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;

import se.jolo.prototypenavigator.deserializers.RouteAdapter;
import se.jolo.prototypenavigator.model.Route;

/**
 * Created by Joel on 2016-02-11.
 */
public class JsonMapper {

    private static final String TAG = "JsonMapper";
    private final Gson gson;
    private final FileLoadAndConvert fileLoadAndConvert;

    public JsonMapper(Context context) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Route.class, new RouteAdapter());
        gson = builder.create();
        fileLoadAndConvert = new FileLoadAndConvert(context);
    }

    public Route objectifyRoute() throws IOException, JSONException {
        return gson.fromJson(fileLoadAndConvert.xmlToJson(), Route.class);
    }
}
