package com.example.maze;

import java.util.List;

import javax.swing.text.Position;

public class AbstarctMaze {

    public Position TOP_LEFT = new Position(-200,100);
    public Position BOTTOM_RIGHT = new Position(100,-200);
    private List<Obstacle> obstacles;

    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    /**
     * Sets the list of obstacles in the world.
     *
     * @param obstacleList The list of obstacles to set.
     */
    public void setObstacles(List<Obstacle> obstacleList){
        this.obstacles = obstacleList;
    }
    
}
