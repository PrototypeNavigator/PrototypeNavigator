package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.jolo.prototypenavigator.model.OdrRecipient;

public class OdrRecipientAdapter implements JsonDeserializer<OdrRecipient> {

    @Override
    public OdrRecipient deserialize(JsonElement jsonElement, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        int amount = json.get("amount").getAsInt();
        String type = json.get("type").getAsString();

        return new OdrRecipient(amount, type);
    }
}
