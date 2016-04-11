package se.jolo.prototypenavigator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.XML;

import java.io.IOException;

import se.jolo.prototypenavigator.adapters.json.DeliveryPointAdapter;
import se.jolo.prototypenavigator.adapters.json.OdrRecipientAdapter;
import se.jolo.prototypenavigator.adapters.json.ResidentAdapter;
import se.jolo.prototypenavigator.adapters.json.RouteAdapter;
import se.jolo.prototypenavigator.adapters.json.AuditInfoAdapter;
import se.jolo.prototypenavigator.adapters.json.RouteItemAdapter;
import se.jolo.prototypenavigator.adapters.json.ServiceAdapter;
import se.jolo.prototypenavigator.adapters.json.StopPointAdapter;
import se.jolo.prototypenavigator.adapters.json.StopPointItemAdapter;
import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.model.DeliveryPoint;
import se.jolo.prototypenavigator.model.OdrRecipient;
import se.jolo.prototypenavigator.model.Resident;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.Service;
import se.jolo.prototypenavigator.model.StopPoint;
import se.jolo.prototypenavigator.model.StopPointItem;

public class JsonMapper {

    private static final String LOG_TAG = "JsonMapper";
    private static Gson gson;

    public JsonMapper() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Route.class, new RouteAdapter());
        builder.registerTypeAdapter(AuditInfo.class, new AuditInfoAdapter());
        builder.registerTypeAdapter(RouteItem.class, new RouteItemAdapter());
        builder.registerTypeAdapter(StopPoint.class, new StopPointAdapter());
        builder.registerTypeAdapter(StopPointItem.class, new StopPointItemAdapter());
        builder.registerTypeAdapter(DeliveryPoint.class, new DeliveryPointAdapter());
        builder.registerTypeAdapter(OdrRecipient.class, new OdrRecipientAdapter());
        builder.registerTypeAdapter(Resident.class, new ResidentAdapter());
        builder.registerTypeAdapter(Service.class, new ServiceAdapter());

        gson = builder.create();
    }

    public Route objectifyRoute(String xmlString) throws IOException, JSONException {
        return gson.fromJson(xmlToJson(xmlString), Route.class);
    }

    public String xmlToJson(String xmlString) throws IOException, JSONException {
        return XML.toJSONObject(xmlString).toString();
    }

    public static Gson getGson() {
        return gson;
    }
}