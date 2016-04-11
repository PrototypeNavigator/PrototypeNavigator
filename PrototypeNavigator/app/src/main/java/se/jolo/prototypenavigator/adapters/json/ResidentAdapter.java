package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.jolo.prototypenavigator.model.Resident;

public class ResidentAdapter implements JsonDeserializer<Resident> {

    @Override
    public Resident deserialize(JsonElement jsonElement, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        String firstname = "";
        String lastname = "";

        if (json.get("firstName") != null) {
            if (!json.get("firstName").isJsonNull()) {
                firstname = json.get("firstName").getAsString();
            }
        }
        if (json.get("lastName") != null) {
            if (!json.get("lastName").isJsonNull()) {
                lastname = json.get("lastName").getAsString();
            }
        }

        return new Resident(firstname, lastname);
    }
}
