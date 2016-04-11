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
import se.jolo.prototypenavigator.model.OdrRecipient;
import se.jolo.prototypenavigator.model.Resident;
import se.jolo.prototypenavigator.utils.JsonMapper;

public class DeliveryPointAdapter implements JsonDeserializer<DeliveryPoint> {

    @Override
    public DeliveryPoint deserialize(JsonElement jsonElement, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {

        JsonObject json = jsonElement.getAsJsonObject();

        boolean odr = json.get("odr").getAsBoolean();

        JsonObject jsonOdrRecipients = json.getAsJsonObject("odrRecipients");

        List<OdrRecipient> odrRecipients = new ArrayList<>();

        if (jsonOdrRecipients.get("odrRecipient").isJsonArray()) {
            JsonArray jsonOdrRecipientsAsJsonArray = jsonOdrRecipients.getAsJsonArray("odrRecipient");
            for (int i = 0; i < jsonOdrRecipientsAsJsonArray.size(); i++) {
                JsonObject jsonObject = jsonOdrRecipientsAsJsonArray.get(i).getAsJsonObject();
                odrRecipients.add(JsonMapper.getGson().fromJson(jsonObject, OdrRecipient.class));
            }
        } else {
            JsonObject jsonOdrRecipientsAsObject = jsonOdrRecipients.getAsJsonObject("odrRecipient");
            odrRecipients.add(JsonMapper.getGson().fromJson(jsonOdrRecipientsAsObject, OdrRecipient.class));
        }

        JsonObject jsonResident = null;

        if (json.getAsJsonObject("residents") != null) {
            jsonResident = json.getAsJsonObject("residents");
        }

        List<Resident> residents = new ArrayList<>();

        if (jsonResident != null) {
            if (jsonResident.get("resident") != null) {
                if (jsonResident.get("resident").isJsonArray()) {
                    JsonArray jsonResidentArray = jsonResident.getAsJsonArray("resident");
                    for (int i = 0; i < jsonResidentArray.size(); i++) {
                        JsonObject jsonObject = jsonResidentArray.get(i).getAsJsonObject();
                        residents.add(JsonMapper.getGson().fromJson(jsonObject, Resident.class));
                    }
                } else {
                    JsonObject jsonObjectResident = jsonResident.getAsJsonObject("resident");
                    residents.add(JsonMapper.getGson().fromJson(jsonObjectResident, Resident.class));
                }
            }
        }

        return new DeliveryPoint(odr, odrRecipients, residents);
    }
}
