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

    public static RouteItem jsonToRouteItem(JsonObject json) {

        int order = json.get("order").getAsInt();
        int primaryStopPointItemUuid = json.get("primaryStopPointItemUuid").getAsInt();
        StopPoint stopPoint = jsonToStopPoint(json.getAsJsonObject("stopPoint"));

        JsonObject jsonObjectStop = json.getAsJsonObject("stopPointItems");

        List<StopPointItem> stopPointItems = new ArrayList<>();

        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////  stopPointItem kan vara  ett jsonObjekt och  en jsonArray                /////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        if(jsonObjectStop.get("stopPointItem").isJsonArray()) {
            JsonArray jsonStopPointItems = jsonObjectStop.getAsJsonArray("stopPointItem");
            for (int i = 0; i < jsonStopPointItems.size(); i++) {
                JsonObject jsonStop = jsonStopPointItems.get(i).getAsJsonObject();
                stopPointItems.add(jsonToStopPointItem(jsonStop));
            }
        }else {
            JsonObject jsonStopPointItemAsObject = jsonObjectStop.getAsJsonObject("stopPointItem");
            stopPointItems.add(jsonToStopPointItem(jsonStopPointItemAsObject));
        }
        return new RouteItem(order, primaryStopPointItemUuid, stopPoint, stopPointItems);
    }

    public static DeliveryPoint jsonToDeliveryPoint(JsonObject json) {

        boolean odr = json.get("odr").getAsBoolean();

        JsonObject jsonOdrRecipients = json.getAsJsonObject("odrRecipients");

        List<OdrRecipient> odrRecipients = new ArrayList<>();

        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////      OdrRecipients kan vara  ett jsonObjekt och  en jsonArray            /////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        if(jsonOdrRecipients.get("odrRecipient").isJsonArray()) {
            JsonArray jsonOdrRecipientsAsJsonArray = jsonOdrRecipients.getAsJsonArray("odrRecipient");
            for (int i = 0; i < jsonOdrRecipientsAsJsonArray.size(); i++) {
                JsonObject jsonObject = jsonOdrRecipientsAsJsonArray.get(i).getAsJsonObject();
                odrRecipients.add(jsonToOdrRecipient(jsonObject));
            }
        }else{
            JsonObject jsonOdrRecipientsAsObject = jsonOdrRecipients.getAsJsonObject("odrRecipient");
            odrRecipients.add(jsonToOdrRecipient(jsonOdrRecipientsAsObject));
        }

        JsonObject jsonResident = json.getAsJsonObject("residents");

        List<Resident> residents = new ArrayList<>();

        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////      resident kan vara  ett jsonObjekt och  en jsonArray                 /////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        if(jsonResident.get("resident").isJsonArray()) {
            JsonArray jsonResidentArray = jsonResident.getAsJsonArray("resident");
            for (int i = 0; i < jsonResidentArray.size(); i++) {
                JsonObject jsonObject = jsonResidentArray.get(i).getAsJsonObject();
                residents.add(jsonToResident(jsonObject));
            }
        }else{
            JsonObject jsonObjectResident = jsonResident.getAsJsonObject("resident");
            residents.add(jsonToResident(jsonObjectResident));
        }

        return new DeliveryPoint(odr, odrRecipients, residents);
    }

    public static StopPointItem jsonToStopPointItem(JsonObject json) {

        Service service = null;
        String uuid = json.get("uuid").getAsString();
        String type = json.get("type").getAsString();
        String name = json.get("name").getAsString();
        String deliveryAddress = json.get("deliveryAddress").getAsString();
        int deliveryPostalCode = json.get("deliveryPostalCode").getAsInt();
        float easting = json.get("easting").getAsFloat();
        float northing = json.get("northing").getAsFloat();
        String freeText = json.get("freeText").getAsString();
        String plannedArrivalTime = json.get("plannedArrivalTime").getAsString();
        String plannedDepartureTime = null;
        int validityDays = json.get("validityDays").getAsInt();

        if(json.get("plannedDepartureTime")!=null){
            plannedDepartureTime = json.get("plannedDepartureTime").getAsString();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////  deliveryPoint kan vara  ett jsonObjekt eller  en jsonArray eller en primitiv    //////
        ////////////////////////////////////////////////////////////////////////////////////////////
        
        List <DeliveryPoint> deliveryPoints = new ArrayList<>();
        if(json.get("deliveryPoints").isJsonObject()) {
            JsonObject jsonDeliveryPoint = json.getAsJsonObject("deliveryPoints");
            if(jsonDeliveryPoint.get("deliveryPoint").isJsonArray()){
                JsonArray jsonDeliverPointArray = jsonDeliveryPoint.getAsJsonArray("deliveryPoint");
                for (int i = 0; i < jsonDeliverPointArray.size(); i++) {
                    JsonObject jsonObject = jsonDeliverPointArray.get(i).getAsJsonObject();
                    deliveryPoints.add(jsonToDeliveryPoint(jsonObject));
                }
            }else {
            JsonObject jsonDeliveryPoints = jsonDeliveryPoint.getAsJsonObject("deliveryPoint");
            deliveryPoints.add(jsonToDeliveryPoint(jsonDeliveryPoints));}
        }
        if(json.get("service")!=null){
         service = jsonToService(json.getAsJsonObject("service"));
        }

        return new StopPointItem(uuid, type, name, deliveryAddress, deliveryPostalCode,
                easting, northing, freeText, plannedArrivalTime, plannedDepartureTime,
                validityDays, deliveryPoints, service);
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



    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateString);
    }
}
