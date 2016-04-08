package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.jolo.prototypenavigator.adapters.JsonToObject;
import se.jolo.prototypenavigator.model.StopPoint;

public class StopPointAdapter implements JsonDeserializer<StopPoint> {

    @Override
    public StopPoint deserialize(JsonElement jsonElement, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        return JsonToObject.jsonToStopPoint(json);
    }
}
