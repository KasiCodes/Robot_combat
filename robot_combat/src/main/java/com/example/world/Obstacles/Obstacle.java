package com.example.world.Obstacles;

import javax.swing.text.Position;

public interface Obstacle {
        /**
     * Get X coordinate of bottom left corner of obstacle.
     * @return x coordinate
     */
    int getBottomLeftX();

    /**
     * Get Y coordinate of bottom left corner of obstacle.
     * @return y coordinate
     */
    int getBottomLeftY();

    /**
     * Gets the side of an obstacle (assuming square obstacles)
     * @return the length of one side in nr of steps
     */
    int getSize();

    /**
     * Checks if this obstacle blocks access to the specified position.
     * @param position the position to check
     * @return return `true` if the x,y coordinate falls within the obstacle's area
     */
    boolean blocksPosition(Position position);

    /**
     * Checks if this obstacle blocks the path that goes from coordinate (x1, y1) to (x2, y2).
     * Since our robot can only move in horizontal or vertical lines (no diagonals yet), we can assume that either x1==x2 or y1==y2.
     * @param a first position
     * @param b second position
     * @return `true` if this obstacle is in the way
     */
    boolean blocksPath(Position a, Position b);

    /**
     * Checks if this obstacle overlaps with another obstacle at a specific position.
     *
     * @param position The position to check for overlap.
     * @return true if this obstacle overlaps with another at the position, false otherwise.
     */
    boolean blocksObstacle(Position position);


    /**
     * Gets the type of the obstacle.
     *
     * @return The type of the obstacle.
     */
    IWorld.ObstacleType getType();
    
}
