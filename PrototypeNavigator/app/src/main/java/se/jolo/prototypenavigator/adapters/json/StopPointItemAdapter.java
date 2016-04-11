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

import se.jolo.prototypenavigator.model.DeliveryPoint;
import se.jolo.prototypenavigator.model.Service;
import se.jolo.prototypenavigator.model.StopPointItem;
import se.jolo.prototypenavigator.utils.JsonMapper;

public class StopPointItemAdapter implements JsonDeserializer<StopPointItem> {

    @Override
    public StopPointItem deserialize(JsonElement jsonElement, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        Service service = null;
        String uuid = json.get("uuid").getAsString();
        String type = json.get("type").getAsString();
        String name = json.get("name").getAsString();
        String deliveryAddress = json.get("deliveryAddress").getAsString();
        int deliveryPostalCode = json.get("deliveryPostalCode").getAsInt();
        float easting = json.get("easting").getAsFloat();
        float northing = json.get("northing").getAsFloat();
        String freeText = "";
        String plannedArrivalTime = "";
        String plannedDepartureTime = "";
        int validityDays = json.get("validityDays").getAsInt();

        if (json.get("freetext") != null) {
            freeText = json.get("freeText").getAsString();
        }

        if (json.get("plannedDepartureTime") != null) {
            plannedDepartureTime = json.get("plannedDepartureTime").getAsString();
        }

        if (json.get("plannedArrivalTime") != null) {
            plannedArrivalTime = json.get("plannedArrivalTime").getAsString();
        }

        List<DeliveryPoint> deliveryPoints = new ArrayList<>();
        if (json.get("deliveryPoints").isJsonObject()) {
            JsonObject jsonDeliveryPoint = json.getAsJsonObject("deliveryPoints");
            if (jsonDeliveryPoint.get("deliveryPoint").isJsonArray()) {
                JsonArray jsonDeliverPointArray = jsonDeliveryPoint.getAsJsonArray("deliveryPoint");
                for (int i = 0; i < jsonDeliverPointArray.size(); i++) {
                    JsonObject jsonObject = jsonDeliverPointArray.get(i).getAsJsonObject();
                    deliveryPoints.add(JsonMapper.getGson().fromJson(jsonObject, DeliveryPoint.class));
                }
            } else {
                JsonObject jsonDeliveryPoints = jsonDeliveryPoint.getAsJsonObject("deliveryPoint");
                deliveryPoints.add(JsonMapper.getGson().fromJson(jsonDeliveryPoints, DeliveryPoint.class));
            }
        }
        if (json.get("service") != null) {
            service = JsonMapper.getGson().fromJson(json.getAsJsonObject("servie"), Service.class);
        }

        return new StopPointItem(uuid, type, name, deliveryAddress, deliveryPostalCode,
                easting, northing, freeText, plannedArrivalTime, plannedDepartureTime,
                validityDays, deliveryPoints, service);
    }
}
