package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;

import se.jolo.prototypenavigator.adapters.JsonToObject;
import se.jolo.prototypenavigator.model.Route;

/*  */
public class RouteAdapter implements JsonDeserializer<Route> {

    @Override
    public Route deserialize(JsonElement jsonElement, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject json = jsonElement.getAsJsonObject();

            return JsonToObject.jsonToRoute(json.getAsJsonObject("route"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}