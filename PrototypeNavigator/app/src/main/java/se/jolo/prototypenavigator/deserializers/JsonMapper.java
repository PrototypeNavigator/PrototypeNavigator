package se.jolo.prototypenavigator.deserializers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.model.DeliveryOffice;
import se.jolo.prototypenavigator.model.DeliveryPoint;
import se.jolo.prototypenavigator.model.OdrRecipient;
import se.jolo.prototypenavigator.model.Resident;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.Service;
import se.jolo.prototypenavigator.model.StopPoint;
import se.jolo.prototypenavigator.model.StopPointItem;
import se.jolo.prototypenavigator.utils.FileLoader;
import se.jolo.prototypenavigator.utils.FulHack;

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
        builder.registerTypeAdapter(DeliveryOffice.class, new DeliveryOfficeAdapter());
        builder.registerTypeAdapter(DeliveryPoint.class, new DeliveryPointAdapter());
        builder.registerTypeAdapter(OdrRecipient.class, new OdrRecipientAdapter());
        builder.registerTypeAdapter(Resident.class, new ResidentAdapter());
        builder.registerTypeAdapter(RouteItem.class, new RouteItemAdapter());
        builder.registerTypeAdapter(Service.class, new ServiceAdapter());
        builder.registerTypeAdapter(StopPoint.class, new StopPointAdapter());
        builder.registerTypeAdapter(StopPointItem.class, new StopPointItemAdapter());
        gson = builder.create();
        this.context = context;
        fileLoader = new FileLoader(context);
    }

    public void objectify() throws IOException, JSONException {
        //JSONObject json = fileLoader.getAsJson();
        Route route = gson.fromJson(fileLoader.xmlToString(), Route.class);
        Log.e(TAG, route.getType());
    }
}
