package com.example.robot;

import javax.swing.text.Position;

public class Robot {
    private int shields;
    private Position position;
    private Direction currentDirection;
    private RobotStatus status;
    private final String name;
    private final Make make;
    private int shots;
    private int maxShields;

    public Robot(String name, Position position, Make make) {
        this.position = position;
        this.name = name;
        this.status = RobotStatus.NORMAL;
        this.currentDirection = Direction.NORTH;
        this.make = make;
        this.shots = make.getNumShots();
        this.shields = make.getShieldStrength();
        this.maxShields = shields;
    }
    public synchronized boolean isActive() {
        return this.status != RobotStatus.DEAD && this.shields >= 0;
    }
    public synchronized boolean canFire() {
        return isActive() && shots > 0;
    }
    public synchronized void fireShot() {
        if (canFire()) {
            shots--;
            System.out.println(name + " shoots! Remaining ammo: " + shots);
        } else System.out.println("No shots left");
    }
    public FireResult fire(IWorld world) {
        if (canFire()) {
            System.out.println("Firing... Initial shots: " + shots);
            fireShot();
            System.out.println("Shots after decrement: " + shots);
            // Use the shot distance from the make
            int maxDistance = make.getShotDistance();

            // Check for hit within range of the MAX_BULLET_DISTANCE
            Robot hitRobot = world.findRobotInRange(position, maxDistance, currentDirection);

            if (hitRobot != null) {
                System.out.println("Potential Hit Robot Position: " + hitRobot.getPosition());
                int distance = position.distanceTo(hitRobot.getPosition());
                if (distance <= maxDistance) {
                    boolean isShieldDepleted = hitRobot.reduceShield(1); // Decrease shield of the target robot
                    if(isShieldDepleted) world.removeRobot(hitRobot);// Remove robot from world if shield is depleted
                    return new FireResult(true, hitRobot, distance, shots,isShieldDepleted); // Return hit result with distance and remaining shots
                }
            }
            return new FireResult(false, null, 0, shots,false); // Return miss result with no distance and remaining shots
        }
        return new FireResult(false, null, 0, shots,false); // Return miss result if no shots are left
    }
    public void setMaxShields(int maxShields) {
        this.maxShields = maxShields;
        this.shields = maxShields;
    }
    public synchronized RobotStatus getStatus() {
        return this.status;
    }

    public synchronized Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public synchronized void setDirection(Direction newDirection){
        this.currentDirection = newDirection;
    }

    public synchronized Position getPosition() {
        return this.position;
    }

    public synchronized void setPosition(Position position){
        this.position = position;
    }

    public void setStatus(RobotStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public UpdateResponse move(String command, String[] nrSteps, TextWorld world){
        UpdateResponse response = command.equalsIgnoreCase("forward") ? world.updatePosition(Integer.parseInt(nrSteps[0]), this) : world.updatePosition(-Integer.parseInt(nrSteps[0]), this);
        if (response.equals(UpdateResponse.FAILED_PITFALL)){
            this.die(true);
            world.removeRobot(this);
        } else {
            this.setStatus(RobotStatus.NORMAL);
        }
        return response;
    }
    public void turn(String[] arguments, TextWorld world) {
        if(isActive()){
            boolean isRight;
            isRight = arguments[0].equalsIgnoreCase("right");
            world.updateDirection(isRight, this);
            this.setStatus(RobotStatus.NORMAL);
        }
    }
    public int getShields() {
        return shields;
    }

    public synchronized int getShots() {
        return shots;
    }

    public void reload() {
        this.shots = this.make.getNumShots(); // reload shots
        this.status = RobotStatus.NORMAL;
    }

    public void repair() {
        this.shields = this.maxShields; // repair shield strength
        this.status = RobotStatus.NORMAL;
    }
    public void die(boolean suddenDeath) {
        if (suddenDeath){
            this.status = RobotStatus.DEAD;
        }
        if (shields < 0){
            this.status = RobotStatus.DEAD;
            this.shields=0;
        }
    }
    public boolean reduceShield(int amount) {
        shields -= amount;
        if (shields < 0) {
            die(false);
            shields = 0;
            return true; // Indicates that the shield is depleted
        }
        return false;
    }
    public static class FireResult {
        public boolean hit;
        public Robot targetRobot;
        public int distance;
        public int shotsLeft;
        public final boolean shieldDepleted;

        public FireResult(boolean hit, Robot targetRobot, int distance, int shotsLeft,boolean shieldDepleted) {
            this.hit = hit;
            this.targetRobot = targetRobot;
            this.distance = distance;
            this.shotsLeft = shotsLeft;
            this.shieldDepleted = shieldDepleted;
        }
    }
    
}
