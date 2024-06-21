package com.example.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputFilter.Config;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Multiserver {
    private static TextWorld world;
    private static final ArrayList<Socket> clientSockets = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        if (args.length > 0 && args[0].equals("config"))
                    Config.createConfigFileFromInput();
    
        File file = new File("config.json");
        if (!file.exists()){
            try{
                Config.createDefaultConfigFile();
            }catch (IOException e){
                System.out.println("Could not create config file.");
            }
        }
        JsonNode config = Protocol.interpretJSONtoObjectNode(file).path("world");

        int width = config.path("width").asInt();
        int height = config.path("height").asInt();
        int numberOfObstacles = config.path("numberOfObstacles").asInt();
        int visibility = config.path("visibility").asInt();
        int repairTime = config.path("repairTime").asInt();
        int reloadTime = config.path("reloadTime").asInt();
        int maxShieldStrength = config.path("maxShieldStrength").asInt();


        world = new TextWorld(
            new CustomMaze(numberOfObstacles,
            new Position(-width/2,height/2),
            new Position(width/2,-height/2)),
            visibility,
            repairTime,
            reloadTime,
            maxShieldStrength);

            System.out.println("World created with size: " + width + "x" + height + " with " +
            numberOfObstacles + " obstacle(s)." +
            "\nVisibility: " + visibility + " kliks" +
            "\nRepair time: " + repairTime + "s\nReload time: " + reloadTime + "s" +
            "\nMax shield strength: " + maxShieldStrength + "\n");
    
        ServerSocket s = new ServerSocket( SimpleServer.PORT);
        System.out.println("Server details");
        System.out.println("IP address: " + InetAddress.getLocalHost().getHostAddress()+ "\tPort: " + SimpleServer.PORT);

        System.out.println("Server running & waiting for client connections.");
        serverCommands(s);

        while(!s.isClosed()) {
            try {
                Socket socket = s.accept();
                clientSockets.add(socket);
                System.out.println("Connection: " + socket);

                Runnable r = new SimpleServer(socket, world);
                Thread task = new Thread(r);
                task.start();
            } catch(IOException ex) {
                System.out.println("Shutting down server...");
            }
        }
        System.out.println("Server shutdown");
    }
 private static void serverCommands(ServerSocket socket){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message;
                    System.out.println("Enter a command: ");
                    while ((message = in.readLine()) != null) {
                        if (message.equalsIgnoreCase("help")) {
                            HelpCommand.serverCommands();
                        } else if (message.equalsIgnoreCase("quit")) {
                            socket.close();
                            for (Socket s : clientSockets) {
                                s.close();
                            }
                            return;
                        } else if (message.equalsIgnoreCase("dump")) {
                            dumpCommand();
                        } else if (message.equalsIgnoreCase("robots")) {
                            getRobots(true);
                        }else System.out.println("Unsupported command\n");
                        System.out.println("Enter a command: ");
                    }
                } catch (IOException ex) {
                    System.out.println("Shutting down single client server");
                }
            }
        });
        task.start();
    }
    private static void dumpCommand(){
        //show obstacles in world
        world.showObstacles();
        //show robots in world
        getRobots(false);
        display2DWorld();
    }
    private static void display2DWorld(){
        int width = world.getWidth();
        int height = world.getHeight();
        String[][] grid = new String[height][width];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = ".";
            }
        }
         List<Obstacle> obstacles = world.getObstacles();
        for (Obstacle obstacle : obstacles) {
            int size = obstacle.getSize();
            int startX = obstacle.getBottomLeftX() + width / 2;
            int startY = height / 2 - obstacle.getBottomLeftY();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = startX + i;
                int y = startY - j;
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    if (obstacle.getType() == IWorld.ObstacleType.MOUNTAIN){
                        grid[y][x] = AbstractWorld.COLOR_MOUNTAIN_TEXT + "▣" + AbstractWorld.RESET; // Use a single character for the square obstacle
                    }else if (obstacle.getType() == IWorld.ObstacleType.PIT){
                        grid[y][x] = AbstractWorld.COLOR_PIT_TEXT + "▦" + AbstractWorld.RESET; // Use a single character for the square obstacle
                    }else if (obstacle.getType() == IWorld.ObstacleType.LAKE) {
                        grid[y][x] = AbstractWorld.COLOR_LAKE_TEXT + "◙" + AbstractWorld.RESET; // Use a single character for the square obstacle
                    }
                }
            }
        }
    }
    List<Robot> robots = world.getRobots();
        for (Robot robot : robots) {
            Position pos = robot.getPosition();
            int x = pos.getX() + width / 2;
            int y = height / 2 - pos.getY();
            if (x >= 0 && x < width && y >= 0 && y < height) {
                grid[y][x] = AbstractWorld.COLOR_ROBOT + robot.getName().charAt(0) + AbstractWorld.RESET; // Mark robots with 'R'
            }
        }
        System.out.print("+  ");
        for (int j = 0; j < width; j++) {
            System.out.print("--");
        }
        System.out.println(" +");

        for (int i = 0; i < height; i++) {
            System.out.print("|  ");
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println(" |");
        }

        System.out.print("+  ");
        for (int j = 0; j < width; j++) {
            System.out.print("--");
        }
        System.out.println(" +");
    
}   
}
