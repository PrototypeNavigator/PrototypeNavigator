package se.jolo.prototypenavigator.adapters.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import se.jolo.prototypenavigator.model.AuditInfo;

/*  */
public class AuditInfoAdapter implements JsonDeserializer<AuditInfo> {

    @Override
    public AuditInfo deserialize(JsonElement jsonElement, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();

        Date createdAt = null;

        try {
            createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(json.get("createdAt").getAsString());
        } catch (ParseException e) { e.printStackTrace(); }

        String createdBy = json.get("createdBy").getAsString();

        return new AuditInfo(createdAt, createdBy);
    }
}
