package com.Robot_world.protocol.response;

import com.Robot_world.protocol.Protocol;
import com.Robot_world.robot.RobotStatus;

public class InvalidStateResponse {
       public static String invalidStateResponse(RobotStatus status){
        ObjectNode responseMessage = Protocol.createObjectNode();
        responseMessage.put("result", "ERROR");

        ObjectNode data = Protocol.createObjectNode();
        data.put("message", status.toString());
        responseMessage.set("data", data);

        return responseMessage.toString();
    }
    
}
