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

import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.StopPoint;
import se.jolo.prototypenavigator.model.StopPointItem;
import se.jolo.prototypenavigator.utils.JsonMapper;

public class RouteItemAdapter implements JsonDeserializer<RouteItem>{

    @Override
    public RouteItem deserialize(JsonElement jsonElement, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        int order = json.get("order").getAsInt();
        String primaryStopPointItemUuid = json.get("primaryStopPointItemUuid").getAsString();
        StopPoint stopPoint = JsonMapper.getGson().fromJson(json.getAsJsonObject("stopPoint"), StopPoint.class);

        JsonObject jsonObjectStop = json.getAsJsonObject("stopPointItems");

        List<StopPointItem> stopPointItems = new ArrayList<>();

        if (jsonObjectStop.get("stopPointItem").isJsonArray()) {
            JsonArray jsonStopPointItems = jsonObjectStop.getAsJsonArray("stopPointItem");
            for (int i = 0; i < jsonStopPointItems.size(); i++) {
                JsonObject jsonStop = jsonStopPointItems.get(i).getAsJsonObject();
                stopPointItems.add(JsonMapper.getGson().fromJson(jsonStop, StopPointItem.class));
            }
        } else {
            JsonObject jsonStopPointItemAsObject = jsonObjectStop.getAsJsonObject("stopPointItem");
            stopPointItems.add(JsonMapper.getGson().fromJson(jsonStopPointItemAsObject, StopPointItem.class));
        }

        return new RouteItem(order, primaryStopPointItemUuid, stopPoint, stopPointItems);
    }
}
