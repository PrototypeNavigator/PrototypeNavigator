package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.jolo.prototypenavigator.model.Service;

public class ServiceAdapter implements JsonDeserializer<Service> {

    @Override
    public Service deserialize(JsonElement jsonElement, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        String agreementArrivalTime = "";
        String agreementDepartureTime = "";
        boolean delivery = json.get("delivery").getAsBoolean();
        boolean pickup = json.get("pickup").getAsBoolean();
        String products = "";
        String serviceCode = "";
        String serviceName = "";

        if (json.get("agreementArrivalTime") != null) {
            if (!json.get("agreementArrivalTime").isJsonNull()) {
                agreementArrivalTime = json.get("agreementArrivalTime").getAsString();
            }
        }
        if (json.get("agreementDepartureTime") != null) {
            if (!json.get("agreementDepartureTime").isJsonNull()) {
                agreementDepartureTime = json.get("agreementDepartureTime").getAsString();
            }
        }

        if (json.get("products") != null) {
            if (!json.get("products").isJsonNull()) {
                products = json.get("products").getAsString();
            }
        }
        if (json.get("serviceCode") != null) {
            if (!json.get("serviceCode").isJsonNull()) {
                serviceCode = json.get("serviceCode").getAsString();
            }
        }
        if (json.get("serviceName") != null) {
            if (!json.get("serviceName").isJsonNull()) {
                serviceName = json.get("serviceName").getAsString();
            }
        }

        return new Service(agreementArrivalTime, agreementDepartureTime, delivery,
                pickup, products, serviceCode, serviceName);
    }
}
