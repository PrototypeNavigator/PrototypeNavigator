package se.jolo.prototypenavigator.utils;

import java.util.HashMap;

import se.jolo.prototypenavigator.model.Instruction;
/* The InstructionTranslator class decodes instruction data to a readable string. */
public class InstructionTranslator {
    private String instructionCode;
    private String streetName;
    private static final HashMap<String, String> MANEUVERS;
    static {
        MANEUVERS = new HashMap<>();
        MANEUVERS.put("0","Okänd manöver");
        MANEUVERS.put("1","Fortsätt på ");
        MANEUVERS.put("2","Sväng svagt höger <>");
        MANEUVERS.put("3","Sväng höger ");
        MANEUVERS.put("4","Sväng skarpt höger <>");
        MANEUVERS.put("5","Gör en u-sväng <>");
        MANEUVERS.put("6","Sväng skarpt vänster <>");
        MANEUVERS.put("7","Sväng vänster till ");
        MANEUVERS.put("8","Sväng svgt vänster till <>");
        MANEUVERS.put("9","Du har nått ett stop");
        MANEUVERS.put("10","Fortsätt <>");
        MANEUVERS.put("11-1","I rondellen, ta den 1:a utfarten");
        MANEUVERS.put("11-2","I rondellen, ta den 2:a utfarten");
        MANEUVERS.put("11-3","I rondellen, ta den 3:e utfarten");
        MANEUVERS.put("11-4","I rondellen, ta den 4:e utfarten");
        MANEUVERS.put("11-5","I rondellen, ta den 5:e utfarten");
        MANEUVERS.put("11-6","I rondellen, ta den 6:e utfarten");
        MANEUVERS.put("11-7","I rondellen, ta den 7:e utfarten");
        MANEUVERS.put("11-8","I rondellen, ta den 8:e utfarten");
        MANEUVERS.put("11-9","I rondellen, ta den 9:e utfarten");
        MANEUVERS.put("15","Du har nått din destination");
    }
    public InstructionTranslator(String instructionCode, String streetName){
        this.instructionCode = instructionCode;
        this.streetName = streetName;
    }

    public String getInstruction(){
        return setInstruction();
    }

    private String setInstruction(){
        String instruction=null;
        if (streetName.equals("")){
            instruction = MANEUVERS.get(instructionCode).replaceFirst("<>", "");
        }else{
            if(instructionCode.equals("10")){
                instruction = MANEUVERS.get(instructionCode).replaceFirst("<>", "på  " + streetName);
            } else if(instructionCode.equals("5")){
                instruction = MANEUVERS.get(instructionCode).replaceFirst("<>", "vid " + streetName);
            }else {
                instruction = MANEUVERS.get(instructionCode).replaceFirst("<>", "till " + streetName);
            }
        }

        return instruction.toString();
    }

}


