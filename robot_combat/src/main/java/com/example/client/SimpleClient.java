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

            }
         
        }

    }
    
}
