package se.jolo.prototypenavigator.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.model.DeliveryOffice;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.StopPointItem;

/**
 * Created by Joel on 2016-02-11.
 */
public class RouteAdapter implements JsonDeserializer<Route> {

    @Override
    public Route deserialize(JsonElement jsonElement, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        AuditInfo auditInfo = context.deserialize(json.get("autidInfo"),
                AuditInfo.class);
        DeliveryOffice deliveryOffice = context.deserialize(json.get("deliveryOffice"),
                DeliveryOffice.class);
        int name = json.get("name").getAsInt();
        String type = json.get("type").getAsString();
        String uuid = json.get("uuid").getAsString();
        int validityDays = json.get("validityDays").getAsInt();
        List<RouteItem> routeItems = context.deserialize(json.get("routeItems"),
                RouteItem.class);
        List<StopPointItem> stopPointItems = context.deserialize(json.get("stopPointItems"),
                StopPointItem.class);

        return new Route(auditInfo, deliveryOffice, name, type, uuid,
                validityDays, routeItems, stopPointItems);
    }
}
