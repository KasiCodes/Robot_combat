package com.example.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SimpleServer implements Runnable{
    public static final int PORT = 5000;
    private final BufferedReader in;
    private final PrintStream out;
    private final String clientMachine;
    private final TextWorld world;
    private Robot robot;

public SimpleServer(Socket socket, TextWorld world) throws IOException {
        this.clientMachine = socket.getInetAddress().getHostName();
        this.world = world;
        System.out.println("Connection from " + clientMachine);

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        System.out.println("Waiting for client...");
    }
    public void setRobot(Robot robot) {
        this.robot = robot;
    }
    public Robot getRobot() {
        return robot;
    }
    public TextWorld getWorld(){return world;}

    @Override
    public void run() {
        try {
            String messageFromClient;
            while((messageFromClient = in.readLine()) != null) {
                System.out.println("Message \"" + messageFromClient + "\" from " + clientMachine);
                String response = ServerProtocol.processRequest(messageFromClient, this);
                out.println(response);
            }
        } catch(IOException ex) {
            System.out.println("Shutting down robot: " + robot.getName());
        } finally {
            closeQuietly();
        }
    }
    private void closeQuietly() {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.close();
    }

    
    
}
