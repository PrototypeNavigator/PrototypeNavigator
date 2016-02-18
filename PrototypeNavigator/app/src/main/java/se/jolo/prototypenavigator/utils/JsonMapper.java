package se.jolo.prototypenavigator.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.XML;

import java.io.IOException;

import se.jolo.prototypenavigator.deserializers.RouteAdapter;
import se.jolo.prototypenavigator.model.Route;

/**
 * Created by Joel on 2016-02-11.
 */
public final class JsonMapper {

    private static final String TAG = "JsonMapper";
    private final Gson gson;

    public JsonMapper() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Route.class, new RouteAdapter());
        gson = builder.create();
    }

    public Route objectifyRoute(String xmlString) throws IOException, JSONException {
        return gson.fromJson(xmlToJson(xmlString), Route.class);
    }

    public String xmlToJson(String xmlString) throws IOException, JSONException {
        return XML.toJSONObject(xmlString).toString();
    }
}
