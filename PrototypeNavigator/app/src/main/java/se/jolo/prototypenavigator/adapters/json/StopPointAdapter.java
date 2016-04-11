package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.jolo.prototypenavigator.model.StopPoint;

public class StopPointAdapter implements JsonDeserializer<StopPoint> {

    @Override
    public StopPoint deserialize(JsonElement jsonElement, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        String freeText = "";
        float easting = json.get("easting").getAsFloat();
        float northing = json.get("northing").getAsFloat();
        String type = json.get("type").getAsString();
        String uuid = json.get("uuid").getAsString();
        if (json.get("freeText") != null) {
            freeText = json.get("freeText").getAsString();
        }

        return new StopPoint(easting, northing, type, uuid, freeText);
    }
}
