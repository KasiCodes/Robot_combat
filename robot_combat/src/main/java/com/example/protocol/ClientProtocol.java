package com.example.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ClientProtocol {

    public static String createJSONLaunchRequest(String robotName, String kind, int maxShieldStrength, int maxShots) throws JsonProcessingException {
        ObjectNode requestMessage = Protocol.createObjectNode();

        requestMessage.put("robot", robotName);
        requestMessage.put("command", "launch");

        ArrayNode argumentsArray = Protocol.createArrayNode();
        argumentsArray.add(kind);
        argumentsArray.add(maxShieldStrength);
        argumentsArray.add(maxShots);
        requestMessage.set("arguments", argumentsArray);

        return Protocol.nodeAsString(requestMessage);
    }
    public static String createJSONForwardBackRequest(String robotName, String command, String nrSteps) throws JsonProcessingException {
        ObjectNode requestMessage = Protocol.createObjectNode();
        requestMessage.put("robot", robotName);
        requestMessage.put("command", command);

        ArrayNode argumentsArray = Protocol.createArrayNode();
        if (!nrSteps.isEmpty()){
            argumentsArray.add(nrSteps);
        }
        requestMessage.set("arguments", argumentsArray);
        return Protocol.nodeAsString(requestMessage);
    }
    public static String createJSONTurnRequest(String robotName, String command, String direction) throws JsonProcessingException {
        ObjectNode requestMessage = Protocol.createObjectNode();
        requestMessage.put("robot", robotName);
        requestMessage.put("command", command);

        ArrayNode argumentsArray = Protocol.createArrayNode();
        argumentsArray.add(direction);
        requestMessage.set("arguments", argumentsArray);

        return Protocol.nodeAsString(requestMessage);
    }
}
