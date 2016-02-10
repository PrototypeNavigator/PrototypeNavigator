package se.jolo.prototypenavigator.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.jolo.prototypenavigator.model.AuditInfo;
import se.jolo.prototypenavigator.model.DeliveryOffice;
import se.jolo.prototypenavigator.model.DeliveryPoint;
import se.jolo.prototypenavigator.model.OdrRecipient;
import se.jolo.prototypenavigator.model.Resident;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.model.Service;
import se.jolo.prototypenavigator.model.StopPoint;
import se.jolo.prototypenavigator.model.StopPointItem;

/**
 * Created by Joel on 2016-02-10.
 */
public final class JsonToObject {

    private JsonToObject() {}

    public static Route jsonToRoute(JSONObject json)
            throws JSONException, ParseException {

        AuditInfo auditInfo = jsonToAuditInfo(json.getJSONObject("auditInfo"));
        DeliveryOffice deliveryOffice = jsonToDeliveyOffice(json.getJSONObject("deliveryOffice"));
        int name = json.getInt("name");
        String type = json.getString("type");
        String uuid = json.getString("uuid");
        int validityDays = json.getInt("validityDays");

        List<RouteItem> routeItems = new ArrayList<>();
        JSONArray jsonRouteItems = json.getJSONArray("routeItems");
        for (int i = 0; i < jsonRouteItems.length(); i++) {
            routeItems.add(jsonToRouteItem(jsonRouteItems.getJSONObject(i)));
        }

        List<StopPointItem> stopPointItems = new ArrayList<>();
        JSONArray jsonStopPointItems = json.getJSONArray("stopPointItems");
        for (int i = 0; i < jsonStopPointItems.length(); i++) {
            stopPointItems.add(jsonToStopPointItem(jsonStopPointItems.getJSONObject(i)));
        }

        return new Route(auditInfo, deliveryOffice, name, type,
                uuid, validityDays, routeItems, stopPointItems);
    }

    public static AuditInfo jsonToAuditInfo(JSONObject json)
            throws JSONException, ParseException {

        Date createdAt = stringToDate(json.get("createdAt").toString());
        String createdBy = json.getString("createdBy");

        return new AuditInfo(createdAt, createdBy);
    }

    public static DeliveryOffice jsonToDeliveyOffice(JSONObject json)
            throws JSONException {

        String name = json.getString("name");
        String uuid = json.getString("uuid");

        return new DeliveryOffice(name, uuid);
    }

    public static DeliveryPoint jsonToDeliveryPoint(JSONObject json) throws JSONException {

        boolean odr = json.getBoolean("odr");
        List<OdrRecipient> odrRecipients = new ArrayList<>();
        JSONArray jsonOdrRecipients = json.getJSONArray("odrRecipients");
        for (int i = 0; i < jsonOdrRecipients.length(); i++) {
            odrRecipients.add(jsonToOdrRecipient(jsonOdrRecipients.getJSONObject(i)));
        }

        List<Resident> residents = new ArrayList<>();
        JSONArray jsonResidents = json.getJSONArray("residents");
        for (int i = 0; i < jsonResidents.length(); i++) {
            residents.add(jsonToResident(jsonResidents.getJSONObject(i)));
        }

        return new DeliveryPoint(odr, odrRecipients, residents);
    }

    public static OdrRecipient jsonToOdrRecipient(JSONObject json) throws JSONException {

        int amount = json.getInt("amount");
        String type = json.getString("type");

        return new OdrRecipient(amount, type);
    }

    public static Resident jsonToResident(JSONObject json) throws JSONException {

        String firstname = json.getString("firstname");
        String lastname = json.getString("lastname");

        return new Resident(firstname, lastname);
    }

    public static RouteItem jsonToRouteItem(JSONObject json) throws JSONException {

        int order = json.getInt("order");
        int primaryStopPointItemUuid = json.getInt("primaryStopPointItemUuid");
        StopPoint stopPoint = jsonToStopPoint(json.getJSONObject("stopPoint"));

        List<StopPointItem> stopPointItems = new ArrayList<>();
        JSONArray jsonStopPointItems = json.getJSONArray("stopPointItems");
        for (int i = 0; i < jsonStopPointItems.length(); i++) {
            stopPointItems.add(jsonToStopPointItem(jsonStopPointItems.getJSONObject(i)));
        }

        return new RouteItem(order, primaryStopPointItemUuid, stopPoint, stopPointItems);
    }

    public static Service jsonToService(JSONObject json) throws JSONException {

        String agreementArrivalTime = json.getString("agreementArrivalTime");
        String agreementDepartureTime = json.getString("agreementDepartureTime");
        boolean delivery = json.getBoolean("delivery");
        boolean pickup = json.getBoolean("pickup");
        String products = json.getString("products");
        String serviceCode = json.getString("serviceCode");
        String serviceName = json.getString("serviceName");

        return new Service(agreementArrivalTime, agreementDepartureTime, delivery, pickup,
                products, serviceCode, serviceName);
    }

    public static StopPoint jsonToStopPoint(JSONObject json) throws JSONException {

        float easting = (float) json.getDouble("easting");
        float northing = (float) json.getDouble("northing");
        String type = json.getString("type");
        String uuid = json.getString("uuid");
        String freeText = json.getString("freeText");

        return new StopPoint(easting, northing, type, uuid, freeText);
    }

    public static StopPointItem jsonToStopPointItem(JSONObject json) throws JSONException {

        String uuid = json.getString("uuid");
        String type = json.getString("type");
        String name = json.getString("name");
        String deliveryAddress = json.getString("deliveryAddress");
        int deliveryPostalCode = json.getInt("deliveryPostalCode");
        float easting = (float) json.getDouble("easting");
        float northing = (float) json.getDouble("northing");
        String freeText = json.getString("freeText");
        String plannedArrivalTime = json.getString("plannedArrivalTime");
        String plannedDepartureTime = json.getString("plannedDepartureTime");
        int validityDays = json.getInt("validityDays");

        List<DeliveryPoint> deliveryPoints = new ArrayList<>();
        JSONArray jsonDeliveryPoints = json.getJSONArray("deliveryPoints");
        for (int i = 0; i < jsonDeliveryPoints.length(); i++) {
            deliveryPoints.add(jsonToDeliveryPoint(jsonDeliveryPoints.getJSONObject(i)));
        }

        Service service = jsonToService(json.getJSONObject("service"));

        return new StopPointItem(uuid, type, name, deliveryAddress, deliveryPostalCode,
                easting, northing, freeText, plannedArrivalTime, plannedDepartureTime,
                validityDays, deliveryPoints, service);
    }


    private static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateString);
    }
}
