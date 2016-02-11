package se.jolo.prototypenavigator.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.model.DeliveryOffice;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.StopPointItem;
import se.jolo.prototypenavigator.utils.FulHack;

/**
 * Created by Joel on 2016-02-11.
 */
public class RouteAdapter implements JsonDeserializer<Route> {

    @Override
    public Route deserialize(JsonElement jsonElement, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        try {
            return FulHack.jsonToRoute(json.getAsJsonObject("route"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
