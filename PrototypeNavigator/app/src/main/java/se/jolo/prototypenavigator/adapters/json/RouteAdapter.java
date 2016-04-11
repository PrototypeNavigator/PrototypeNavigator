package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.utils.JsonMapper;

/*  */
public class RouteAdapter implements JsonDeserializer<Route> {

    @Override
    public Route deserialize(JsonElement jsonElement, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        json = json.getAsJsonObject("route");

        AuditInfo auditInfo = JsonMapper.getGson().fromJson(json.getAsJsonObject("autidInfo"), AuditInfo.class);

        String deliveryOffice = json.get("deliveryOfficeUuid").getAsString();
        String name = json.get("name").getAsString();
        String type = json.get("type").getAsString();
        String uuid = json.get("uuid").getAsString();
        int validityDays = 0 /*json.get("validityDays").getAsInt()*/;

        JsonObject routeItems = json.getAsJsonObject("routeItems");

        List<RouteItem> routeItem = new ArrayList<>();
        JsonArray jsonRouteItem = routeItems.getAsJsonArray("routeitem");
        for (int i = 0; i < jsonRouteItem.size(); i++) {
            JsonObject jsonObject = jsonRouteItem.get(i).getAsJsonObject();
            routeItem.add(JsonMapper.getGson().fromJson(jsonObject, RouteItem.class));
        }

        return new Route(auditInfo, deliveryOffice, name, type, uuid, validityDays, routeItem);
    }
}