package se.jolo.prototypenavigator.deserializers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.utils.FileLoader;

/**
 * Created by Joel on 2016-02-11.
 */
public final class JsonMapper {

    private static final String TAG = "JsonMapper";
    private final Gson gson;
    private final Context context;
    private final FileLoader fileLoader;

    public JsonMapper(Context context) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Route.class, new RouteAdapter());
        builder.registerTypeAdapter(AuditInfo.class, new AuditInfoAdapter());
        builder.serializeNulls();
        gson = builder.create();
        this.context = context;
        fileLoader = new FileLoader(context);
    }

    public void objectify() throws IOException, JSONException {
        JSONObject json = fileLoader.getAsJson();
        Route route = gson.fromJson(json.toString(), Route.class);
        Log.v(TAG, route.getType());
    }
}
