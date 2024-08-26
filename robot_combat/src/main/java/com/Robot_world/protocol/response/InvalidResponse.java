package com.Robot_world.protocol.response;

import com.Robot_world.protocol.Protocol;

public class InvalidResponse {
    
    public static String unsupportedCommandResponse(){
        ObjectNode responseMessage = Protocol.createObjectNode();
        responseMessage.put("result", "ERROR");

        ObjectNode data = Protocol.createObjectNode();
        data.put("message", "Unsupported command");
        responseMessage.set("data", data);

        return responseMessage.toString();
    }

    public static String unsupportedArgumentsResponse(){
        ObjectNode responseMessage = Protocol.createObjectNode();
        responseMessage.put("result", "ERROR");

        ObjectNode data = Protocol.createObjectNode();
        data.put("message", "Could not parse arguments");
        responseMessage.set("data", data);

        return responseMessage.toString();
    }

    
}
