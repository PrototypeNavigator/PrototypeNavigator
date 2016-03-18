package se.jolo.prototypenavigator.utils;

import java.util.HashMap;

import se.jolo.prototypenavigator.model.Instruction;

/**
 * Created by Holstad on 18/03/16.
 */
public class InstructionTranslator {
    private String language;
    private static final HashMap<String, String> MANEUVERS;
    static {
        MANEUVERS = new HashMap<>();
        MANEUVERS.put("0","Okänd manöver");
        MANEUVERS.put("1","Fortsätt på ");
        MANEUVERS.put("2","Sväng svagt till höger på ");
        MANEUVERS.put("3","Sväng till höger på ");
        MANEUVERS.put("4","Sväng skarpt till höger på ");
        MANEUVERS.put("5","Gör en u-sväng vid ");
        MANEUVERS.put("6","Sväng skarpt till vänster på ");
        MANEUVERS.put("7","Okänd manöver");
        MANEUVERS.put("8","Okänd manöver");
        MANEUVERS.put("9","Okänd manöver");
        MANEUVERS.put("10","Okänd manöver");
        MANEUVERS.put("11-1","Okänd manöver");
        MANEUVERS.put("11-2","Okänd manöver");
        MANEUVERS.put("11-3","Okänd manöver");
        MANEUVERS.put("11-4","Okänd manöver");
        MANEUVERS.put("11-5","Okänd manöver");
        MANEUVERS.put("11-6","Okänd manöver");
        MANEUVERS.put("11-7","Okänd manöver");
        MANEUVERS.put("11-8","Okänd manöver");
        MANEUVERS.put("11-9","Okänd manöver");
        MANEUVERS.put("9","Okänd manöver");
        MANEUVERS.put("9","Okänd manöver");
        MANEUVERS.put("9","Okänd manöver");
        MANEUVERS.put("9","Okänd manöver");
        MANEUVERS.put("9","Okänd manöver");
        MANEUVERS.put("9","Okänd manöver");
    }
    public InstructionTranslator(){}

}

/*
directions = new HashMap<String, String>();
        directions.put("6","Turn sharp left< on %s>");
        directions.put("7","Turn left< on %s>");
        directions.put("8","Turn slight left< on %s>");
        directions.put("9","You have reached a waypoint of your trip");
        directions.put("10","<Go on %s>");
        directions.put("11-1","Enter roundabout and leave at first exit< on %s>");
        directions.put("11-2","Enter roundabout and leave at second exit< on %s>");
        directions.put("11-3","Enter roundabout and leave at third exit< on %s>");
        directions.put("11-4","Enter roundabout and leave at fourth exit< on %s>");
        directions.put("11-5","Enter roundabout and leave at fifth exit< on %s>");
        directions.put("11-6","Enter roundabout and leave at sixth exit< on %s>");
        directions.put("11-7","Enter roundabout and leave at seventh exit< on %s>");
        directions.put("11-8","Enter roundabout and leave at eighth exit< on %s>");
        directions.put("11-9","Enter roundabout and leave at nineth exit< on %s>");
        directions.put("15","You have reached your destination");*/
