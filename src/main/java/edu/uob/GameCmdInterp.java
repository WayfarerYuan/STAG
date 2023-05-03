package edu.uob;

import edu.uob.entities.*;
import edu.uob.entities.Character;
import edu.uob.entities.Furniture;
import edu.uob.entities.Path;

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
            case "health":
                return player.getHealthStr();
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
                    resMsg += "\t" + character.getName() + "("+ character.getDescription() + ")\n";
            }
        }
        if (artefacts.size() > 0) {
            resMsg += "Artefacts: \n";
            for (Artefact artefact : artefacts) {
                resMsg += "\t" + artefact.getName() + "("+ artefact.getDescription() + ")\n";
            }
        }
        if (furnitures.size() > 0) {
            resMsg += "Furnitures: \n";
            for (Furniture furniture : furnitures) {
                resMsg += "\t" + furniture.getName() + "("+ furniture.getDescription() + ")\n";
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
    private String exeCustomAction(GameAction action) {
        // Load the action
        ArrayList<GameEntity> subjects = action.getSubjects();
        ArrayList<GameEntity> consumed = action.getConsumed();
        ArrayList<GameEntity> produced = action.getProduced();
        String narration = action.getNarration().getName();
        String resMsg = "";
        // Check if subjects exist in the location
        if (!checkSubjects(subjects) && !subjects.isEmpty()) {
            resMsg = "Some of the subjects are not in this location.";
            return resMsg;
        }
        // Check if consumed exist in the inventory or location
        if (!checkSubjects(consumed) && !consumed.isEmpty()) {
            resMsg = "Some of the consumed items are not in your inventory or this location.";
            // DEBUG:
            System.out.println("[Interp] Consumed issue");
            System.out.println("[Interp] Consumed items: " + consumed.size());
            for (GameEntity item : consumed) {
                System.out.println("[Interp] (should) Consumed item: " + item.getName());
            }
            return resMsg;
        }
        // consume the consumed items
        for (GameEntity item : consumed) {
            if (item instanceof Artefact) {
                player.getLocation().removeArtefact((Artefact) item);
                // also remove the item from the inventory
                player.removeFromInventory(item);
            } else if (item instanceof Character) {
                player.getLocation().removeCharacter((Character) item);
            } else if (item instanceof Furniture) {
                System.out.println("Removing furniture " + item.getName() + " from location " + player.getLocation().getName());
                player.getLocation().removeFurniture((Furniture) item);
                System.out.println("After action, remaining furniture in location " + player.getLocation().getName() + ":");
                for (Furniture furniture : player.getLocation().getFurnitures()) {
                    System.out.println("\t" + furniture.getName());
                }
            } else if (item instanceof Path) {
                player.getLocation().removePath((Path) item);
            } else if (item instanceof Location) {
                player.getLocation().removePath(new Path(player.getLocation().getName(), ((Location) item).getName()));
                // remove the reverse path
                ((Location) item).removePath(new Path(((Location) item).getName(), player.getLocation().getName()));
            } else if (item instanceof Character) {
                player.getLocation().removeCharacter((Character) item);
            } else if (item.getName().toLowerCase().equals("health")) {
                if (player.getHealth() == 1) {
                    resMsg = "You died and lost all of your items, you must return to the start of the game";
                    player.setHealth(player.getHealth() - 1);
                    return resMsg;
                }
                player.setHealth(player.getHealth() - 1);
            }
        }
        // produce the produced items
        for (GameEntity item : produced) {
            if (item instanceof Artefact) {
                player.getLocation().addArtefact((Artefact) item);
            } else if (item instanceof Character) {
                player.getLocation().addCharacter((Character) item);
            } else if (item instanceof Furniture) {
                player.getLocation().addFurniture((Furniture) item);
            } else if (item instanceof Path) {
                player.getLocation().addPath((Path) item);
            } else if (item instanceof Location) {
                player.getLocation().addPath(new Path(player.getLocation().getName(), ((Location) item).getName()));
                // add the reverse path
                ((Location) item).addPath(new Path(((Location) item).getName(), player.getLocation().getName()));
            } else if (item instanceof Character) {
                player.getLocation().addCharacter((Character) item);
            } else if (item.getName().toLowerCase().equals("health")) {
                player.setHealth(player.getHealth() + 1);
            }
        }
        resMsg = narration;
        return resMsg;
    }

    private boolean checkSubjects(ArrayList<GameEntity> subjects) {
        boolean res = false;
        for (GameEntity subject : subjects) {
            if(!checkSubject(subject)) {
                res = false;
                break;
            } else {
                res = true;
            }
        }
        return res;
    }

    private boolean checkSubject(GameEntity subject) {
        boolean res = false;
        if (subject instanceof Artefact) {
            System.out.println("Subject " + subject.getName() + " is an artefact.");
            // check if the artefact is in the player's inventory
            ArrayList<GameEntity> inventory = player.getInventory();
            for (GameEntity item : inventory) {
                if (item instanceof Artefact) {
                    if (item.getName().equals(subject.getName())) {
                        System.out.println("Subject " + subject.getName() + " is in the player's inventory.");
                        res = true;
                        break;
                    }
                }
            }
        } else if (subject instanceof Character) {
            System.out.println("Subject " + subject.getName() + " is a character.");
            // check if the character is in the location
            ArrayList<Character> characters = player.getLocation().getCharacters();
            for (Character character : characters) {
                if (character.getName().equals(subject.getName())) {
                    System.out.println("Subject " + subject.getName() + " is in the player's location.");
                    res = true;
                    break;
                }
            }
        } else if (subject instanceof Furniture) {
            System.out.println("Subject " + subject.getName() + " is a furniture.");
            // check if the furniture is in the location
            ArrayList<Furniture> furnitures = player.getLocation().getFurnitures();
            for (Furniture furniture : furnitures) {
                if (furniture.getName().equals(subject.getName())) {
                    System.out.println("Subject " + subject.getName() + " is in the player's location.");
                    res = true;
                    break;
                }
            }
        } else if (subject instanceof Path) {
            System.out.println("Subject " + subject.getName() + " is a path.");
            // check if the path is in the location
            ArrayList<Path> paths = player.getLocation().getPaths();
            for (Path path : paths) {
                if (path.getDestination().equals(subject.getName())) {
                    System.out.println("Subject " + subject.getName() + " is in the player's location.");
                    res = true;
                    break;
                }
            }
        } else if (subject instanceof Location) {
            System.out.println("Subject " + subject.getName() + " is a location.");
            // check if the location is in the location
            ArrayList<Path> paths = player.getLocation().getPaths();
            for (Path path : paths) {
                if (path.getDestination().equals(subject.getName())) {
                    System.out.println("Subject " + subject.getName() + " is in the player's location.");
                    res = true;
                    break;
                }
            }
        } else if (subject instanceof Character) {
            System.out.println("Subject " + subject.getName() + " is a character.");
            // check if the character is in the location
            ArrayList<Character> characters = player.getLocation().getCharacters();
            for (Character character : characters) {
                if (character.getName().equals(subject.getName())) {
                    System.out.println("Subject " + subject.getName() + " is in the player's location.");
                    res = true;
                    break;
                }
            }
        } else if (subject.getName().toLowerCase().equals("health")) {
            res = true;
        } else {
            System.out.println("Subject " + subject.getName() + " is not a unknown type of subject.");
        }
        return res;
    }

}
