package com.example.world;

import java.util.List;
import java.util.Map;

import com.example.robot.Position;
import com.example.robot.Robot;
import com.example.world.Obstacles.Obstacle;

public interface IWorld {

    /**
     * Enum used to track direction
     */
    enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    enum ObstacleType {
        MOUNTAIN, LAKE, PIT, EDGE, ROBOT
    }

    /**
     * Enum that indicates response for updatePosition request
     */
    enum UpdateResponse {
        SUCCESS, //position was updated successfully
        FAILED_OUTSIDE_WORLD, //robot will go outside world limits if allowed, so it failed to update the position
        FAILED_OBSTRUCTED_OBSTACLE, //robot obstructed by at least one obstacle, thus cannot proceed.
        FAILED_OBSTRUCTED_ROBOT, // robot obstructed by a robot being in that position, thus cannot proceed.
        FAILED_PITFALL, // robot falls into a pit, leading to death.
    }


    /**
     * Updates the position of the robot in the world.
     *
     * @param nrSteps The number of steps to move.
     * @param robot The robot to move.
     * @return The response indicating the result of the position update.
     */
    UpdateResponse updatePosition(int nrSteps, Robot robot);

    /**
     * Updates the direction the robot is facing in the world.
     *
     * @param turnRight Whether the robot should turn right.
     * @param robot The robot to update.
     */
    void updateDirection(boolean turnRight, Robot robot);

    /**
     * Checks if the new position will be allowed, i.e. falls within the constraints of the world, and does not overlap an obstacle.
     * @param position the position to check
     * @return true if it is allowed, else false
     */
    boolean isWithinBoundary(Position position);

    /**
     * Checks if a position is blocked by an obstacle.
     *
     * @param newPosition The position to check.
     * @return True if the position is blocked by an obstacle, false otherwise.
     */
    boolean isPositionBlockedByObstacle(Position newPosition);

    /**
     * Checks if a position is blocked by a robot.
     *
     * @param newPosition The position to check.
     * @return True if the position is blocked by a robot, false otherwise.
     */
    boolean isPositionBlockedByRobot(Position newPosition);

    /**
     * Checks if the path between two positions is blocked by an obstacle.
     *
     * @param a The starting position.
     * @param b The ending position.
     * @return True if the path is blocked by an obstacle, false otherwise.
     */
    boolean isPathBlockedByObstacle(Position a, Position b);

    /**
     * Checks if the path between two positions is blocked by a robot.
     *
     * @param a The starting position.
     * @param b The ending position.
     * @return True if the path is blocked by a robot, false otherwise.
     */
    boolean isPathBlockedByRobot(Position a, Position b);

    /**
     * @return the list of obstacles, or an empty list if no obstacles exist.
     */
    List<Obstacle> getObstacles();

    /**
     * Gives opportunity to world to draw or list obstacles.
     */
    void showObstacles();
    /**
     * Retrieves the list of robots currently in the world.
     * @return List of robots
     */

    List<Robot> getRobots();

    /**
     * Checks if there is enough space to launch a new robot.
     *
     * @return True if there is enough space, false otherwise.
     */
    boolean enoughSpace();

    /**
     * Checks if a robot name is valid (i.e., not already taken by another robot).
     *
     * @param robotName The name to check.
     * @return True if the name is valid, false otherwise.
     */
    boolean validName(String robotName);
    /**
     * Adds a robot to the world.
     * @param robot the robot to add
     */

    void addRobot(Robot robot);

    /**
     * Gets an empty spot in the world where a robot can be placed.
     *
     * @return A position representing an empty spot.
     */
    Position getEmptySpot();
    /**
     * Retrieves the top-left corner position of the world.
     * @return Position of the top-left corner
     */
    Position getTOP_LEFT();
    /**
     * Retrieves the bottom-right corner position of the world.
     * @return Position of the bottom-right corner
     */
    Position getBOTTOM_RIGHT();
    /**
     * Updates the world's obstacles.
     */
    void updateWorldObstacles();
    /**
     * Retrieves the obstacles in the world as a map.
     * @return Map of positions and obstacles
     */
    Map<Position, Object> getWorldObstacles();

    /**
     * Finds a robot within a specified range from a given position and in a specific direction.
     * @param position the position from which to search
     * @param range the maximum range to search
     * @param direction the direction to check for robots
     * @return the closest robot within range and in the specified direction, or null if no robot is found
     */
    Robot findRobotInRange(Position position, int range,Direction direction);
    

    // String fire(Robot firingRobot, int range);
    void removeRobot(Robot robot); 

    
    
}
