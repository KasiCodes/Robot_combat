package com.Robot_world.robot;
import com.Robot_world.protocol.Protocol;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;

public class Make {
       private String name;
    private int shieldStrength;
    private int numShots;
    private int shotDistance;

    public Make(String name) throws IOException {
        File file = new File("config.json");
        if (!file.exists()){
            try{
                Config.createDefaultConfigFile();
            }catch (IOException e){
                System.out.println("Could not create config file.");
            }
        }

        JsonNode makes = Protocol.interpretJSONtoObjectNode(file).path("make");

        int defaultShieldStrength = 0, defaultNumShots = 0, defaultShotDistance = 0;
        boolean foundMake = false;
        for (JsonNode make : makes){
            String makeName = make.path("name").asText();

            if (name.equals(makeName)){
                foundMake = true;
                this.name = makeName;
                this.shieldStrength = make.path("shieldStrength").asInt();
                this.numShots = make.path("numShots").asInt();
                this.shotDistance = make.path("shotDistance").asInt();
                break;
            }

            if (makeName.equals("explorer")){
                defaultShieldStrength = make.path("shieldStrength").asInt();
                defaultNumShots = make.path("numShots").asInt();
                defaultShotDistance = make.path("shotDistance").asInt();
            }
        }

        if (!foundMake) {
            this.name = "explorer";
            this.shieldStrength = defaultShieldStrength;
            this.numShots = defaultNumShots;
            this.shotDistance = defaultShotDistance;
        }
    }
    public String getName() {
        return name;
    }
    public int getShieldStrength() {
        return shieldStrength;
    }
    public int getNumShots() {
        return numShots;
    }
    public int getShotDistance() {
        return shotDistance;
    }
    
}
