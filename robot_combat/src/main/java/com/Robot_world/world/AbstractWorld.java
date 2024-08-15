package com.Robot_world.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Robot_world.robot.Position;
import com.Robot_world.robot.Robot;
import com.Robot_world.world.Obstacles.Obstacle;

public abstract class AbstractWorld implements IWorld {

    public static final String COLOR_MOUNTAIN_TEXT = "\u001B[38;5;70m"; //Color to represent mountain obstacles.
    public static final String COLOR_LAKE_TEXT = "\u001B[38;5;27m"; //Color to represent lake obstacles.
    public static final String COLOR_PIT_TEXT = "\u001B[38;5;137m"; //Color to represent pit obstacles.
    public static final String COLOR_ROBOT = "\u001B[38;5;125m"; //Color to represent robots.
    public static final String RESET = "\u001B[0m"; //Default color of font, used to revert color back to original color.

    protected final CustomMaze maze;
    protected final List<Robot> robotList = new ArrayList<>();
    protected Map<Position, Object> worldObstacles;
    private final int visibility;
    private final int repairTime;
    private final int reloadTime;
    private final int maxShieldStrength;

    /**
     * Constructs an AbstractWorld object.
     *
     * @param maze The maze layout of the world.
     * @param visibility The visibility range for the robots.
     * @param repairTime The time taken to repair a robot.
     * @param reloadTime The time taken to reload a robot's shots.
     * @param maxShieldStrength The maximum shield strength for the robots.
     */
    public AbstractWorld(CustomMaze maze, int visibility, int repairTime, int reloadTime, int maxShieldStrength){
        this.maze = maze;
        this.visibility = visibility;
        this.repairTime = repairTime;
        this.reloadTime = reloadTime;
        this.maxShieldStrength = maxShieldStrength;
    }

    @Override
    public boolean isWithinBoundary(Position position) {
        return position.isIn(maze.TOP_LEFT, maze.BOTTOM_RIGHT);
    }

    /**
     * Checks if the new position is blocked by any obstacle in the world.
     *
     * @param newPosition The position to check if blocked by obstacles.
     * @return True if the position is blocked by any obstacle, false otherwise.
     */
    public boolean isPositionBlockedByObstacle(Position newPosition) {
        for (Obstacle obstacle : getObstacles()) {
            if (obstacle.blocksPosition(newPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a pit obstacle in the path between the current position of the robot and a new position.
     *
     * @param newPosition The new position to check against pits.
     * @param robot The robot whose current position is used as the starting point of the path.
     * @return true if there is a pit obstacle in the path, false otherwise.
     */
    public boolean isPitInPath(Position newPosition, Robot robot){
        Position robotPosition = robot.getPosition();
        int startX = robotPosition.getX();
        int startY = robotPosition.getY();
        int endX = newPosition.getX();
        int endY = newPosition.getY();

        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        for (Obstacle obstacle : getObstacles()) {
            if (obstacle.getType() == ObstacleType.PIT){
                int size = obstacle.getSize();
                int x = obstacle.getBottomLeftX();
                int y = obstacle.getBottomLeftY();
                for (int i = minX; i <= maxX; i++) {
                    for (int j = minY; j <= maxY; j++) {
                        if (i >= x && i < x + size && j >= y && j < y + size) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isPositionBlockedByRobot(Position newPosition){
        for (Robot robot: this.robotList){
            if (robot.getPosition().equals(newPosition)) return true;
        }
        return false;
    }

    /**
     * Checks if a given position is blocked by any pit (obstacle).
     *
     * <p>This method iterates through all the obstacles and determines if the
     * specified position falls within the boundaries of the obstacles.
     * Each obstacle is assumed to be square-shaped with a defined size.
     *
     * @param position The position to be checked.
     * @return true if the position is blocked by a pit (obstacle), false otherwise.
     */
    public boolean isPositionBlockedByPit(Position position){
        int x = position.getX();
        int y = position.getY();
        for (Obstacle obstacle: getObstacles()){
            if (x >= obstacle.getBottomLeftX() && x < obstacle.getBottomLeftX() + obstacle.getSize() &&
                    y >= obstacle.getBottomLeftY() && y < obstacle.getBottomLeftY() + obstacle.getSize()){
                return true;
            }
        }
        return false;
    }


    public boolean isPathBlockedByObstacle(Position a, Position b){
        // Check if any obstacle blocks the path
        for (Obstacle obstacle : getObstacles()) {
            if (obstacle.blocksPath(a, b)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPathBlockedByRobot(Position a, Position b){
        for (int x = Math.min(a.getX(), b.getX()); x <= Math.max(a.getX(), b.getX()); x++) {
            for (int y = Math.min(a.getY(), b.getY()); y <= Math.max(a.getY(), b.getY()); y++) {
                Position position = new Position(x, y);
                // Position a is the current position of the robot about to move
                // && !a.equals(position) disregards that position when iterating over the path
                // because that position is occupied by the aforementioned.
                if (isPositionBlockedByRobot(position) && !a.equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates a new position based on the number of steps and the robot's current direction.
     *
     * @param nrSteps The number of steps to move.
     * @param robot The robot whose position is to be calculated.
     * @return The new position.
     */
    public Position calculateNewPosition(int nrSteps, Robot robot){
        int newX = robot.getPosition().getX();
        int newY = robot.getPosition().getY();
        switch (robot.getCurrentDirection()){
            case NORTH:
                newY += nrSteps;
                break;
            case EAST:
                newX += nrSteps;
                break;
            case WEST:
                newX -= nrSteps;
                break;
            case SOUTH:
                newY -= nrSteps;
                break;
        }
        return new Position(newX, newY);
    }

    public List<Obstacle> getObstacles() {
        return this.maze.getObstacles();
    }

    public List<Robot> getRobots() {
        return robotList;
    }

    public void addRobot(Robot robot) {
        this.robotList.add(robot);
        updateWorldObstacles();
    }

    public Position getTOP_LEFT(){
        return this.maze.TOP_LEFT;
    }

    public Position getBOTTOM_RIGHT() {
        return this.maze.BOTTOM_RIGHT;
    }

    @Override
    public void updateWorldObstacles() {
        Map<Position, Object> worldObstacles = new HashMap<>();
        for (Robot robot : getRobots()){
            worldObstacles.put(robot.getPosition(), robot);
        }
        for (Obstacle obstacle : getObstacles()) {
            int x = obstacle.getBottomLeftX();
            int y = obstacle.getBottomLeftY();
            worldObstacles.put(new Position(x,y), obstacle);
        }

        this.worldObstacles = worldObstacles;
    }

    @Override
    public Map<Position, Object> getWorldObstacles() {
        updateWorldObstacles();
        return worldObstacles;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public int getRepairTime() {
        return repairTime;
    }

    public int getMaxShieldStrength() {
        return maxShieldStrength;
    }
    public void removeRobot(Robot robot) {
        robot.die(false);
        robotList.remove(robot);
    }
    
}
