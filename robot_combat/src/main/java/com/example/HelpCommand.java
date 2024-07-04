package com.example;

public class HelpCommand {

    public static void help(){
        // HELP COMMAND
        System.out.println(
            "\t=============================================================================\n" +
            "\t|                  Welcome to Group_16's Robot_World:                       |\n" +
            "\t|                                                                           |\n" +
            "\t|  \u001B[34m        ██████   ██████  ██████   ██████  ████████ ███████\u001B[0m               |\n" +
            "\t|  \u001B[34m        ██   ██ ██    ██ ██   ██ ██    ██    ██    ██     \u001B[0m               |\n" +
            "\t|  \u001B[34m        ██████  ██    ██ ██████  ██    ██    ██    ███████\u001B[0m               |\n" +
            "\t|  \u001B[34m        ██   ██ ██    ██ ██   ██ ██    ██    ██         ██\u001B[0m               |\n" +
            "\t|  \u001B[34m        ██   ██  ██████  ██████   ██████     ██    ███████\u001B[0m               |\n" +
            "\t|                                                                           |\n" +
            "\t|           Dive into a world where machines rule, and humans               |\n" +
            "\t|           have been reduced to mere spectators. But fear not,             |\n" +
            "\t|           brave explorer Your mission, should you choose to               |\n" +
            "\t|                                                                           |\n" +
            "\t|           accept it, is to commandeer a cutting-edge robot and            |\n" +
            "\t|           navigate through a labyrinth of high-tech hazards,              |\n" +
            "\t|           cunning traps, and treacherous terrain. Every move              |\n" +
            "\t|           counts, every strategy is tested, and every victory             |\n" +
            "\t|                                                                           |\n" +
            "\t|           brings you one step closer to reclaiming control over           |\n" +
            "\t|           the digital frontier. Are you ready to rise up and              |\n" +
            "\t|           challenge the machine overlords?                                |\n" +
            "\t|                                                                           |\n" +
            "\t|   ROBOT MAKES                                                             |\n" +
            "\t|         sniper     - longer range but limited rounds.                     |\n" +
            "\t|         tank       - more durable with range but limited rounds.          |\n" +
            "\t|         explorer   - a simple well rounded bot.                           |\n" +
            "\t|                                                                           |\n" +
            "\t|   [Launch] your Robot now to embark on your robotic revolution!           |\n" +
            "\t|   # Use the following command to [Launch] your robot:                     |\n" +
            "\t|   [Launch] [Robot_Make] [Your_Name]                                       |\n" +
            "\t=============================================================================\n"
        );
    }

    public static void handleClientCommands() {
        System.out.println(
            "\n The following commands are used to control your robot: \n" +
            "\t|===============================================================|\n" +
            "\t|                 Client_Commands:                              |\n" +
            "\t|===============================================================|\n" +
            "\t|  * [State] = To see the state your robot is in.               |\n" +
            "\t|  * [Turn] <left / right> = To turn your robot left or right.  |\n" +
            "\t|  * [Forward] <num_of_steps> = To move your robot forwards.    |\n" +
            "\t|  * [Back] <num_of_steps> = To move your robot backwards.      |\n" +
            "\t|  * [Look] = Allow robot to look around in the world.          |\n" +
            "\t|  * [Fire] = To fire a shot at an opponent.                    |\n" +
            "\t|  * [Reload] = To reload robot's weapons.                      |\n" +
            "\t|  * [Repair] = To repair robot's shield.                       |\n" +
            "\t|  * [Help] = To display this help menu.                        |\n" +
            "\t|---------------------------------------------------------------|\n"
        );
    }
    public static void serverCommands() {
        System.out.println(
            "\nThe following commands are used to communicate with the server: \n" +
            "\t=================================================================\n" +
            "\t|                Server_Commands:                               |\n" +
            "\t=================================================================\n" +
            "\t| * [QUIT] = disconnects all robots and ends the world.         |\n" +
            "\t| * [Dump] = displays a representation of the world’s state     |\n" +
            "\t|            showing robots, obstacles, and anything else       |\n" +
            "\t|            in the world that you programmed.                  |\n" +
            "\t| * [Robots] = lists all robots in the world including          |\n" +
            "\t|              the robot’s name and state                       |\n" +
            "\t| * [Help] = displays this help menu.                           |\n" +
            "\t=================================================================\n"
        );
    }
    
}
