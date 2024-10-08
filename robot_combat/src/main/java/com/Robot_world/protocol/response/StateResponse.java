package com.Robot_world.protocol.response;

import com.Robot_world.protocol.Protocol;
import com.Robot_world.robot.Robot;

public class StateResponse {
     /**
     * Creates a JSON string representing the state response of a robot.
     *
     * @param robot The robot whose state response is to be generated.
     * @return A JSON string representing the state response.
     */
    public static String createStateResponse(Robot robot) {
        ObjectNode response = Protocol.createObjectNode();
        response.put("result", "OK");
        
        ObjectNode stateNode = Protocol.createObjectNode();
        ArrayNode positionArray = Protocol.createArrayNode();
        positionArray.add(robot.getPosition().getX());
        positionArray.add(robot.getPosition().getY());
        stateNode.set("position", positionArray);
        stateNode.put("direction", robot.getCurrentDirection().toString());
        stateNode.put("shields", robot.getShields());
        stateNode.put("shots", robot.getShots());
        stateNode.put("status", String.valueOf(robot.getStatus()));

        response.set("state", stateNode);
        return response.toString();
    }
    
}
