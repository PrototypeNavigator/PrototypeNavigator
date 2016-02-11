package se.jolo.prototypenavigator.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.jolo.prototypenavigator.model.DeliveryOffice;
import se.jolo.prototypenavigator.utils.FulHack;

/**
 * Created by Joel on 2016-02-11.
 */
public class DeliveryOfficeAdapter implements JsonDeserializer<DeliveryOffice> {
    @Override
    public DeliveryOffice deserialize(JsonElement jsonElement, Type typeOfT,
                                      JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        return FulHack.jsonToDeliveryOffice(json.getAsJsonObject("deliveryOffice"));
    }
}
