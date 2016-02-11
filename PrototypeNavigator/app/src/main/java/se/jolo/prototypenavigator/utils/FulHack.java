package se.jolo.prototypenavigator.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
 * Created by Joel on 2016-02-11.
 */
public final class FulHack {

    public static Route jsonToRoute(JsonObject json) throws ParseException {

        AuditInfo auditInfo = jsonToAuditInfo(json.getAsJsonObject("auditInfo"));
        DeliveryOffice deliveryOffice = jsonToDeliveryOffice(json.getAsJsonObject("deliveryOffice"));
        int name = json.get("name").getAsInt();
        String type = json.get("type").getAsString();
        String uuid = json.get("uuid").getAsString();
        int validityDays = json.get("validityDays").getAsInt();


        JsonObject routeItems = json.getAsJsonObject("routeItems");

        List<RouteItem> routeItem = new ArrayList<>();
        JsonArray jsonRouteItem = routeItems.getAsJsonArray("routeItem");
        for (int i = 0; i < jsonRouteItem.size(); i++) {
            JsonObject jsonObject = jsonRouteItem.get(i).getAsJsonObject();
            routeItem.add(jsonToRouteItem(jsonObject));
        }

        return new Route(auditInfo, deliveryOffice, name, type, uuid, validityDays, routeItem);
    }

    public static AuditInfo jsonToAuditInfo(JsonObject json) throws ParseException {

        Date createdAt = stringToDate(json.get("createdAt").getAsString());
        String createdBy = json.get("createdBy").getAsString();

        return new AuditInfo(createdAt, createdBy);
    }

    public static DeliveryOffice jsonToDeliveryOffice(JsonObject json) {

        String name = json.get("name").getAsString();
        String uuid = json.get("uuid").getAsString();

        return new DeliveryOffice(name, uuid);
    }

    public static DeliveryPoint jsonToDeliveryPoint(JsonObject json) {

        boolean odr = json.get("odr").getAsBoolean();

        List<OdrRecipient> odrRecipients = new ArrayList<>();
        JsonArray jsonOdrRecipients = json.getAsJsonArray("odrRecipients");
        for (int i = 0; i < jsonOdrRecipients.size(); i++) {
            JsonObject jsonObject = jsonOdrRecipients.get(i).getAsJsonObject();
            odrRecipients.add(jsonToOdrRecipient(jsonObject.getAsJsonObject("odrRecipient")));
        }

        List<Resident> residents = new ArrayList<>();
        JsonArray jsonResidents = json.getAsJsonArray("residents");
        for (int i = 0; i < jsonResidents.size(); i++) {
            JsonObject jsonObject = jsonResidents.get(i).getAsJsonObject();
            residents.add(jsonToResident(jsonObject.getAsJsonObject("residents")));
        }

        return new DeliveryPoint(odr, odrRecipients, residents);
    }

    public static OdrRecipient jsonToOdrRecipient(JsonObject json) {

        int amount = json.get("amount").getAsInt();
        String type = json.get("type").getAsString();

        return new OdrRecipient(amount, type);
    }

    public static Resident jsonToResident(JsonObject json) {

        String firstname = json.get("firstname").getAsString();
        String lastname = json.get("lastname").getAsString();

        return new Resident(firstname, lastname);
    }

    public static RouteItem jsonToRouteItem(JsonObject json) {

        //JsonObject jsonObjectBajs = json.getAsJsonObject("routeItem"); //

        int order = json.get("order").getAsInt();
        int primaryStopPointItemUuid = json.get("primaryStopPointItemUuid").getAsInt();
        StopPoint stopPoint = jsonToStopPoint(json.getAsJsonObject("stopPoint"));

        List<StopPointItem> stopPointItems = new ArrayList<>();
        JsonArray jsonStopPointItems = json.getAsJsonArray("stopPointItems");
        for (int i = 0; i < jsonStopPointItems.size(); i++) {
            JsonObject jsonObject = json.get("stopPointItem").getAsJsonObject();
            stopPointItems.add(jsonToStopPointItem(jsonObject.getAsJsonObject("stopPointItem")));
        }

        return new RouteItem(order, primaryStopPointItemUuid, stopPoint, stopPointItems);
    }

    public static Service jsonToService(JsonObject json) {

        String agreementArrivalTime = json.get("agreementArrivalTime").getAsString();
        String agreementDepartureTime = json.get("agreementDepartureTime").getAsString();
        boolean delivery = json.get("delivery").getAsBoolean();
        boolean pickup = json.get("pickup").getAsBoolean();
        String products = json.get("products").getAsString();
        String serviceCode = json.get("serviceCode").getAsString();
        String serviceName = json.get("serviceName").getAsString();

        return new Service(agreementArrivalTime, agreementDepartureTime, delivery,
                pickup, products, serviceCode, serviceName);
    }

    public static StopPoint jsonToStopPoint(JsonObject json) {

        float easting = json.get("easting").getAsFloat();
        float northing = json.get("northing").getAsFloat();
        String type = json.get("type").getAsString();
        String uuid = json.get("uuid").getAsString();
        String freeText = json.get("freeText").getAsString();

        return new StopPoint(easting, northing, type, uuid, freeText);
    }

    public static StopPointItem jsonToStopPointItem(JsonObject json) {

        String uuid = json.get("uuid").getAsString();
        String type = json.get("type").getAsString();
        String name = json.get("name").getAsString();
        String deliveryAddress = json.get("deliveryAddress").getAsString();
        int deliveryPostalCode = json.get("deliveryPostalCode").getAsInt();
        float easting = json.get("easting").getAsFloat();
        float northing = json.get("northing").getAsFloat();
        String freeText = json.get("freeText").getAsString();
        String plannedArrivalTime = json.get("plannedArrivalTime").getAsString();
        String plannedDepartureTime = json.get("plannedDepartureTime").getAsString();
        int validityDays = json.get("validityDays").getAsInt();

        List<DeliveryPoint> deliveryPoints = new ArrayList<>();
        JsonArray jsonDeliveryPoints = json.getAsJsonArray("deliveryPoints");
        for (int i = 0; i < jsonDeliveryPoints.size(); i++) {
            JsonObject jsonObject = jsonDeliveryPoints.get(i).getAsJsonObject();
            deliveryPoints.add(jsonToDeliveryPoint(jsonObject.getAsJsonObject("deliveryPoint")));
        }

        Service service = jsonToService(json.getAsJsonObject("service"));

        return new StopPointItem(uuid, type, name, deliveryAddress, deliveryPostalCode,
                easting, northing, freeText, plannedArrivalTime, plannedDepartureTime,
                validityDays, deliveryPoints, service);
    }

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateString);
    }
}
