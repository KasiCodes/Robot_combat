package com.Robot_world.protocol.response;

import com.Robot_world.protocol.Protocol;
import com.Robot_world.robot.Robot;

public class HelpResponse {

    /**
     * Generates a response message for the help command.
     *
     * @param robot   The robot whose state is being updated.
     * @return A JSON-formatted response message for the turn command.
     */
    public static String helpResponse(Robot robot){
        ObjectNode responseMessage = Protocol.createObjectNode();
        responseMessage.put("result", "Ok");

        ObjectNode data = Protocol.createObjectNode();
        data.put("message", "Help");
        responseMessage.set("data", data);

        return responseMessage.toString();
    }
    
}
