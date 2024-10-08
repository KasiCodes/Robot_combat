package com.Robot_world.protocol.response;

import com.Robot_world.protocol.Protocol;
import com.Robot_world.robot.Robot;
import com.Robot_world.world.AbstractWorld;
import com.Robot_world.world.LookHelper;

public class LookResponse {


    /**
     * Generates a successful response for the look command, including the objects seen around the robot
     * and the current state of the robot.
     *
     * @param robot The robot performing the look operation.
     * @param world The world in which the robot is located.
     * @return A JSON string representing the successful look response.
     */
    public static String lookSuccessfulResponse(Robot robot, AbstractWorld world){
        ObjectNode responseMessage = Protocol.createObjectNode();
        responseMessage.put("result", "OK");
        ObjectNode data = Protocol.createObjectNode();
        LookHelper.lookAround(robot, world);
        ArrayNode objectsArray = LookHelper.getArrayNode(robot, world);

        Position robotPosition = robot.getPosition();
        data.set("objects", objectsArray);
        responseMessage.put("data", data);

        ArrayNode positionArray = Protocol.createArrayNode();
        positionArray.add(robotPosition.getX());
        positionArray.add(robotPosition.getY());

        ObjectNode state = Protocol.createObjectNode();
        state.set("position", positionArray);
        state.put("direction", robot.getCurrentDirection().toString());
        state.put("shields", robot.getShields());
        state.put("shots", robot.getShots());
        state.put("status", robot.getStatus().toString());
        responseMessage.set("state", state);
        return responseMessage.toString();
    }

    /**
     * Generates an error response indicating that the specified robot does not exist in the world.
     *
     * @return A JSON string representing the error response for a non-existent robot.
     */
    public static String robotDoesNotExistResponse(){
        ObjectNode responseMessage = Protocol.createObjectNode();
        responseMessage.put("result", "ERROR");
        ObjectNode data = Protocol.createObjectNode();
        data.put("message", "The given robot name does not exist in this world.");
        responseMessage.set("data", data);
        return responseMessage.toString();
    }
    
}
