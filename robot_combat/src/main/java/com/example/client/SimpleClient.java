package com.example.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {
        static Scanner scanner;
        private static boolean robotLaunched = false;
        public static void main(String[] args) {
            String hostIP = "localhost";
            int port = 5000;
            if (args.length > 1){
                hostIP = args[0];
                port = Integer.parseInt(args[1]);
            }
            try (
            Socket socket = new Socket(hostIP, port);
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()))
        )
        {
            HelpCommand.help();
            String robotName = "";

            while (true){
                System.out.println("\nEnter a command: ");
                String input = scanner.nextLine().toLowerCase();
                String clientRequest;
                String[] instruction = input.split(" ");
                if(robotName.isEmpty() && !instruction[0].equals("launch")) {
                    System.out.println("Oops! Something went wrong, Remember to [Launch] your robot first.");
                    continue;
                }
                if (instruction[0].equals("launch") && robotLaunched) {
                    System.out.println("A robot has already been launched. You cannot launch another robot.");
                    continue;
                }
                clientRequest = ClientProtocol.buildRequestMessage(instruction, robotName);
                out.println(clientRequest);
                out.flush();
                String messageFromServer = in.readLine();
                ObjectNode serverResponse = Protocol.interpretJSONtoObjectNode(messageFromServer);

                switch(instruction[0]){
                    case ("launch") -> {
                        if (serverResponse.path("result").asText().equals("ERROR")) System.out.println(serverResponse.path("data").path("message").asText());
                        else {
                            GraphicsCommand.displayAirplaneGraphic();
                            HelpCommand.handleClientCommands();
                            robotName = instruction[2];
                            robotLaunched = true;
                            JsonNode data = serverResponse.path("data");
                            System.out.println("Successfully launched "+robotName+ " into the world at position "+ data.path("position") +" with "+ serverResponse.path("state").path("shields") + " shields and " + serverResponse.path("state").path("shots") + " shots.");
                            System.out.println("World has visibility of "+ data.path("visibility") + " steps, reload time of "+ data.path("reload")+ "s and repair time of "+ data.path("repair")+"s.");
                        }
                    }
                    case ("forward"), ("back") -> {
                        String message = serverResponse.path("data").path("message").asText();
                        switch (message) {
                            case "FAILED_PITFALL" -> System.out.println(robotName + " has fallen into a pit");
                            case "FAILED_OUTSIDE_WORLD" -> System.out.println(robotName + " cannot go out of bounds");
                            case "FAILED_OBSTRUCTED_OBSTACLE" -> System.out.println(robotName + " is obstructed by an obstacle");
                            case "FAILED_OBSTRUCTED_ROBOT" -> System.out.println(robotName + " is obstructed by a robot");
                            case "RELOAD" -> System.out.println(robotName + " is currently reloading");
                            case "REPAIR" -> System.out.println(robotName + " is currently repairing its shield");
                            case "DEAD" -> System.out.println(robotName + " is dead");
                            case "Could not parse arguments" -> System.out.println("could not parse arguments");
                            default -> System.out.println(robotName + ": moved " + instruction[0] + " - now at position " + serverResponse.path("state").path("position"));
                        }
                    }
                    case ("turn") -> {
                        String message = serverResponse.path("data").path("message").asText();
                        switch (message){
                            case "RELOAD" -> System.out.println(robotName + " is currently reloading");
                            case "REPAIR" -> System.out.println(robotName + " is currently repairing its shield");
                            case "DEAD" -> System.out.println(robotName + " is dead");
                            case "Could not parse arguments" -> System.out.println("could not parse arguments");
                            default -> System.out.println(robotName+ ": turned " + instruction[1]  + " - now facing "+ serverResponse.path("state").path("direction").asText());
                        }
                    }
                    case ("look") -> {

                        if (serverResponse.path("result").asText().equals("ERROR")) System.out.println(serverResponse.path("data").path("message").asText());
                        else {
                            JsonNode data = serverResponse.path("data");
                            JsonNode status = serverResponse.path("state").path("status");
                            if (status.asText().equals("DEAD")){
                                System.out.println(robotName + " is dead");
                            }else {
                                System.out.println(robotName + " saw:");
                                if(data.path("objects").isEmpty()){
                                    System.out.println(" - Nothing...");
                                }
                                for (JsonNode object : data.path("objects")) {
                                    String article = " - a ";
                                    if (object.get("type").asText().equals("EDGE")) {
                                        article = " - the ";
                                    }
                                    System.out.println(article + object.get("type").asText().toLowerCase() + ", " + object.get("distance").asText() + " kliks to the " + object.get("direction") + ".");
                                }
                            }
                        }
                    }
                    case ("state") -> {
                        if (serverResponse.path("result").asText().equals("ERROR")) System.out.println(serverResponse.path("data").path("message").asText());
                        else {
                            JsonNode state = serverResponse.path("state");
                            System.out.println("Robot State:");
                            System.out.println("Position: " + state.path("position"));
                            System.out.println("Direction: " + state.path("direction"));
                            System.out.println("Shields: " + state.path("shields"));
                            System.out.println("Shots: " + state.path("shots"));
                            System.out.println("Status: " + state.path("status"));
                        }
                    }
                    case ("fire") -> {
                        if (!serverResponse.path("result").asText().equals("ERROR")) {
                            JsonNode data = serverResponse.path("data");
                            if (data.path("message").asText().equals("No more shots.")) {
                                System.out.println("No shots left.");
                                continue;
                            }

                            GraphicsCommand.displayGunGraphic();
                            if (data.path("message").asText().equals("Hit")) {
                                System.out.println("Hit another robot!");
                                System.out.println("Distance: " + data.path("distance").asInt());
                                System.out.println("Robot: " + data.path("robot").asText());
                                // Update the number of shots left
                                int shotsLeft = serverResponse.path("state").path("shots").asInt();
                                System.out.println("Shots left: " + shotsLeft);
                            } else if (data.path("message").asText().equals("Miss")) {
                                System.out.println("Missed!");
                                // Update the number of shots left
                                int shotsLeft = serverResponse.path("state").path("shots").asInt();
                                System.out.println("Shots left: " + shotsLeft);
                            }
                        } else {
                            String message = serverResponse.path("data").path("message").asText();
                            switch (message){
                                case "RELOAD" -> System.out.println(robotName + " is currently reloading");
                                case "REPAIR" -> System.out.println(robotName + " is currently repairing its shield");
                                case "DEAD" -> System.out.println(robotName + " is dead");
                                case "Could not parse arguments" -> System.out.println("could not parse arguments");
                                default -> System.out.println(robotName+ " is reloading.");
                            }
                        }
                    }

                }
            }
         
        }

    }
    
}
