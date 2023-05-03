package edu.uob;

import edu.uob.entities.*;
import edu.uob.entities.Character;
//import edu.uob.entities.Character;
//import edu.uob.entities.Furniture;
//import edu.uob.entities.Path;

import java.util.ArrayList;

public class GameCmdInterp {
    private GamePlayer player;
    private GameWorld world;
    private GameAction action;

    public GameCmdInterp(GameWorld world, GamePlayer player, GameAction action) {
        this.player = player;
        this.world = world;
        this.action = action;
    }

    public String interpret() {
        String resMsg = "";
        if (isBuiltInAction(action.getName())) {
            resMsg = exeBuiltInAction(action);
        } else {
            resMsg = exeCustomAction(action);
        }
        return resMsg;
    }

    private boolean isBuiltInAction(String actionName) {
        return actionName.equals("inventory") || actionName.equals("get") || actionName.equals("drop") || actionName.equals("goto") || actionName.equals("look") || actionName.equals("health");
    }

    private String exeBuiltInAction(GameAction action) {
        String actionName = action.getName();
        switch (actionName) {
            case "inventory":
                return printInventory();
            case "get":
                return pickUp(action.getSubjects().get(0));
            case "drop":
                return drop(action.getSubjects().get(0));
            case "goto":
                return goTo(action.getSubjects().get(0).getName());
            case "look":
                return look();
//            case "health":
//                return printHealth();
            default:
                return "Invalid action";
        }
    }

    private String printInventory() {
        String resMsg = "";
        ArrayList<GameEntity> inventory = player.getInventory();
        if (inventory.size() == 0) {
            resMsg = "Your inventory is empty.";
        } else {
            resMsg = "You have the following items in your inventory: \n";
            for (GameEntity item : inventory) {
                resMsg += item.getName() + "\n";
            }
        }
        return resMsg;
    }

    private String pickUp(GameEntity obj) {
        String resMsg = "";
        String objName = obj.getName();
        System.out.println("[Interp] Player: " + player.getName() + ", Action: " + action.getName() + ", Subject: " + objName);
        //player.getLocation().getArtefacts();
        for (GameEntity item : player.getLocation().getArtefacts()) {
            if (item.getName().equals(objName)) {
                // Debug:
                System.out.println("Player " + player.getName() + " is picking up " + objName + ".");
                player.addToInventory(item);
                player.getLocation().removeArtefact((Artefact) item);
                resMsg = "You picked up " + objName + ".";
                break;
            }
        }
        if (resMsg.equals("")) {
            resMsg = "There is no " + objName + " in this location or you cannot get it.";
        }
        return resMsg;
    }

    private String drop(GameEntity obj) {
        String resMsg = "";
        String objName = obj.getName();
        System.out.println("[Interp] Player: " + player.getName() + ", Action: " + action.getName() + ", Subject: " + objName);
        for (GameEntity item : player.getInventory()) {
            if (item.getName().equals(objName)) {
                // Debug:
                System.out.println("Player " + player.getName() + " is dropping " + objName + ".");
                player.getLocation().addArtefact((Artefact) item);
                player.removeFromInventory(item);
                resMsg = "You dropped " + objName + ".";
                break;
            }
        }
        if (resMsg.equals("")) {
            resMsg = "You don't have " + objName + " in your inventory.";
        }
        return resMsg;
    }

    private String look() {
        String resMsg = "";
//        resMsg += player.getLocation().getDescription() + "\n";
//        resMsg += "You can see the following items in this location: \n";
//        for (GameEntity item : player.getLocation().getArtefacts()) {
//            resMsg += item.getName() + "\n";
//        }
//        return resMsg;
        ArrayList<Character> characters = player.getLocation().getCharacters();
        ArrayList<Artefact> artefacts = player.getLocation().getArtefacts();
        ArrayList<Furniture> furnitures = player.getLocation().getFurnitures();
        ArrayList<Path> paths = player.getLocation().getPaths();
        resMsg += "You are in " + player.getLocation().getDescription() + "\n";
        resMsg += "You look around and find: \n";
        if (characters.size() > 0) {
            resMsg += "Characters: \n";
            for (Character character : characters) {
                if (character.getName().equals(player.getName()))
                    resMsg += "\t" + character.getName() + " (You)\n";
                else
                    resMsg += "\t" + character.getName() + "\n";
            }
        }
        if (artefacts.size() > 0) {
            resMsg += "Artefacts: \n";
            for (Artefact artefact : artefacts) {
                resMsg += "\t" + artefact.getName() + "\n";
            }
        }
        if (furnitures.size() > 0) {
            resMsg += "Furnitures: \n";
            for (Furniture furniture : furnitures) {
                resMsg += "\t" + furniture.getName() + "\n";
            }
        }
        if (paths.size() > 0) {
            resMsg += "Paths: \n";
            for (Path path : paths) {
                resMsg += "\t" + path.getSource() + " -> " + path.getDestination() + "\n";
            }
        }
        if (resMsg.equals("")) {
            resMsg = "There's only darkness in your eyes, must be something wrong?";
        }
        return resMsg;
    }

    private String goTo(String destStr) {
        String resMsg = "";
        ArrayList<Location> locations = world.getLocationsList();
        Location dest = null;
        for (Location location : locations) {
            if (location.getName().equals(destStr)) {
                dest = location;
                break;
            }
        }
        ArrayList<Path> paths = player.getLocation().getPaths();
        for (Path path : paths) {
            if (path.getDestination().equals(destStr)) {
                player.goToLocation(dest);
                resMsg = "You are now in " + destStr + ".";
                break;
            }
        }
        if (resMsg.equals("")) {
            resMsg = "You cannot go to " + destStr + " from here.";
        }
        return resMsg;
    }
    private static String exeCustomAction(GameAction action) {
        return "";
    }
}
