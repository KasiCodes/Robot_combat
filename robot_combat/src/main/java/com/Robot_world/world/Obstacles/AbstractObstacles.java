package com.Robot_world.world.Obstacles;

import javax.swing.text.Position;

import com.Robot_world.world.IWorld;

public class AbstractObstacles implements Obstacle {
    private final int bottomLeftX;
    private final int bottomLeftY;
    private final int size;
    private final IWorld.ObstacleType type;

    public AbstractObstacle(int bottomLeftX, int bottomLeftY, int size, IWorld.ObstacleType type) {
        this.bottomLeftX = bottomLeftX;
        this.bottomLeftY = bottomLeftY;
        this.size = size;
        this.type = type;
    }
    @Override
    public int getBottomLeftX() {
        return bottomLeftX;
    }
    @Override
    public int getBottomLeftY() {
        return bottomLeftY;
    }
    @Override
    public int getSize() {
        return size;
    }
    @Override
    public boolean blocksPosition(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= bottomLeftX && x < bottomLeftX + this.getSize() &&
                y >= bottomLeftY && y < bottomLeftY + this.getSize() && this.getType()!= IWorld.ObstacleType.PIT;
    }
    @Override
    public boolean blocksPath(Position a, Position b) {
        for (int x = Math.min(a.getX(), b.getX()); x <= Math.max(a.getX(), b.getX()); x++) {
            for (int y = Math.min(a.getY(), b.getY()); y <= Math.max(a.getY(), b.getY()); y++) {
                if (blocksPosition(new Position(x, y))) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean blocksObstacle(Position position) {
        for (int x = position.getX(); x < position.getX() + this.getSize(); x++) {
            for (int y = position.getY(); y < position.getY() + this.getSize(); y++) {
                if (this.blocksPosition(new Position(x, y))) {
                    return true;
                }
            }
        }
        return false;
    }
    public IWorld.ObstacleType getType(){
        return type;
    }

    
}
