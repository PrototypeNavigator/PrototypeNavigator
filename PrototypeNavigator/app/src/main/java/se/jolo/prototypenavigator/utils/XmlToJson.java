package se.jolo.prototypenavigator.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import se.jolo.prototypenavigator.model.Route;

/**
 * Created by Joel on 2016-02-10.
 */
public final class XmlToJson {

    public static JSONObject stringToJson(String xmlString) throws JSONException {
        return XML.toJSONObject(xmlString);
    }
}
