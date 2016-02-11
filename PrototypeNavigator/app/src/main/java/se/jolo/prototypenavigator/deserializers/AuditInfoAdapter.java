package se.jolo.prototypenavigator.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;

import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.utils.FulHack;

/**
 * Created by Joel on 2016-02-11.
 */
public class AuditInfoAdapter implements JsonDeserializer<AuditInfo> {
    @Override
    public AuditInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return FulHack.jsonToAuditInfo(json.getAsJsonObject());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
