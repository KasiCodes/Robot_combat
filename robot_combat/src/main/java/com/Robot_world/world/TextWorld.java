package com.Robot_world.world;

import robotworld.maze.CustomMaze;
import robotworld.robot.Position;
import robotworld.robot.Robot;
import robotworld.world.Obstacles.Obstacle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a textual representation of the world.
 */
public class TextWorld extends AbstractWorld{
    private int width;
    private int height;

    /**
     * Constructs a TextWorld object with the specified maze, visibility, repair time, reload time, and maximum shield strength.
     *
     * @param maze The maze that defines the world's layout.
     * @param visibility The visibility range for robots in the world.
     * @param repairTime The time required for robots to repair.
     * @param reloadTime The time required for robots to reload.
     * @param maxShieldStrength The maximum shield strength for robots.
     */
    public TextWorld(CustomMaze maze, int visibility, int repairTime, int reloadTime, int maxShieldStrength){
        super(maze, visibility, repairTime, reloadTime, maxShieldStrength);
        this.width = maze.BOTTOM_RIGHT.getX() - maze.TOP_LEFT.getX() + 1;
        this.height = maze.TOP_LEFT.getY() - maze.BOTTOM_RIGHT.getY() + 1;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    @Override
    public synchronized UpdateResponse updatePosition(int nrSteps, Robot robot) {
        Position newPosition = calculateNewPosition(nrSteps, robot);
        if (!isWithinBoundary(newPosition)) {
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        }
        if (isPositionBlockedByObstacle(newPosition)){
            return UpdateResponse.FAILED_OBSTRUCTED_OBSTACLE;
        }
        if (isPathBlockedByObstacle(robot.getPosition(), newPosition)){
            return UpdateResponse.FAILED_OBSTRUCTED_OBSTACLE;
        }
        if (isPositionBlockedByRobot(newPosition)){
            return UpdateResponse.FAILED_OBSTRUCTED_ROBOT;
        }
        if (isPathBlockedByRobot(robot.getPosition(), newPosition)){
            return UpdateResponse.FAILED_OBSTRUCTED_ROBOT;
        }
        if (isPitInPath(newPosition, robot)){
            return UpdateResponse.FAILED_PITFALL;
        }
        robot.setPosition(newPosition);
        return UpdateResponse.SUCCESS;
    }

    @Override
    public synchronized void updateDirection(boolean turnRight, Robot robot) {
        int numDirections = Direction.values().length;
        int currentIndex = robot.getCurrentDirection().ordinal();
        int newIndex;

        if (turnRight) {
            newIndex = (currentIndex + 1) % numDirections;
        } else {
            newIndex = (currentIndex - 1 + numDirections) % numDirections;
        }
        robot.setDirection(Direction.values()[newIndex]);
    }


    @Override
    public void showObstacles() {
        List<Obstacle> obsList = getObstacles();
        if (!obsList.isEmpty()){
            System.out.println("There are some obstacles:");
            for (Obstacle obs : obsList){
                switch (obs.getType()){
                    case PIT -> {
                        System.out.print(" A ");
                        System.out.print(COLOR_PIT_TEXT + "▦ Pit " + RESET);
                    }
                    case MOUNTAIN -> {
                        System.out.print(" A ");
                        System.out.print(COLOR_MOUNTAIN_TEXT + "▣ Mountain " + RESET);
                    }
                    case LAKE -> {
                        System.out.print(" A ");
                        System.out.print(COLOR_LAKE_TEXT + "◙ Lake " + RESET);
                    }
                }
                System.out.println("at position " + obs.getBottomLeftX() + "," + obs.getBottomLeftY() +
                        "(to " + (obs.getBottomLeftX() + obs.getSize()) + "," +
                        (obs.getBottomLeftY() + obs.getSize()) + ")");
            }
        }
    }

    @Override
    public boolean enoughSpace() {
        // iterate over all position in the world
        for (int x = maze.TOP_LEFT.getX(); x <= maze.BOTTOM_RIGHT.getX(); x++) {
            for (int y = maze.BOTTOM_RIGHT.getY(); y <= maze.TOP_LEFT.getY(); y++) {
                Position position = new Position(x, y);
                if (!isPositionBlockedByObstacle(position) && !isPositionBlockedByRobot(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validates if the given name is unique among all robots in the world.
     * @param name The name to be validated.
     * @return true if the name is unique, false otherwise.
     */
    public boolean validName(String name){
        for (Robot robot : robotList){
            if (robot.getName().equals(name)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Position getEmptySpot() {
        // get random positions until an empty spot is found
        // ThreadLocalRandom is a better option than using Random in a multithreaded environment
        while (true) {
            int randomX = ThreadLocalRandom.current().nextInt(maze.TOP_LEFT.getX(), maze.BOTTOM_RIGHT.getX() + 1);
            int randomY = ThreadLocalRandom.current().nextInt(maze.BOTTOM_RIGHT.getY(), maze.TOP_LEFT.getY() + 1);
            Position randomPosition = new Position(randomX, randomY);

            if (!isPositionBlockedByObstacle(randomPosition) && !isPositionBlockedByRobot(randomPosition) && !isPositionBlockedByPit(randomPosition)) {
                return randomPosition;
            }
        }
    }

    /**
 * Finds a robot within a specified range in a given direction from the start position.
 *
 * @param startPosition the position from which to start the search.
 * @param range the maximum distance to search for a robot.
 * @param direction the direction in which to search for the robot.
 * @return the robot found within range and direction, or null if no robot is found.
 */
    @Override
    public Robot findRobotInRange(Position startPosition, int range,Direction direction) {
        for (Robot robot : getRobots()) {
            Position robotPosition = robot.getPosition();
            int distance = startPosition.distanceTo(robotPosition);

            if (distance <= range && isInLineOfFire(startPosition, robotPosition, direction)) {
                return robot;
            }
        }
        return null;
    }

    // Private helper method
    private boolean isInLineOfFire(Position shooter, Position target, Direction direction) {
        switch (direction) {
            case NORTH:
                return shooter.getX() == target.getX() && shooter.getY() < target.getY() && !isPathBlockedByObstacle(shooter, target);
            case EAST:
                return shooter.getY() == target.getY() && shooter.getX() < target.getX() && !isPathBlockedByObstacle(shooter, target);
            case SOUTH:
                return shooter.getX() == target.getX() && shooter.getY() > target.getY() && !isPathBlockedByObstacle(shooter, target);
            case WEST:
                return shooter.getY() == target.getY() && shooter.getX() > target.getX() && !isPathBlockedByObstacle(shooter, target);
            default:
                return false;
        }
    }

}
