package edu.uob;

import edu.uob.entities.Artefact;

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
        return actionName.equals("inv") || actionName.equals("get") || actionName.equals("drop") || actionName.equals("goto") || actionName.equals("look") || actionName.equals("health");
    }

    private String exeBuiltInAction(GameAction action) {
        String actionName = action.getName();
        switch (actionName) {
            case "inventory":
                // debug:
                System.out.println("Player " + player.getName() + " is trying to print inventory.");
                return printInventory();
            case "get":
                return pickUp(action.getSubjects().get(0));
//            case "drop":
//                return drop(action.getObjName());
//            case "goto":
//                return goTo(action.getObjName());
//            case "look":
//                return look();
//            case "health":
//                return printHealth();
            default:
                return "Invalid action";
        }
    }

    private String printInventory() {
        String resMsg = "";
        if (player.getInventory().size() == 0) {
            resMsg = "Your inventory is empty.";
        } else {
            resMsg = "You have the following items in your inventory:\n";
            for (GameEntity item : player.getInventory()) {
                // Debug:
                System.out.println("Player " + player.getName() + " has " + item.getName() + " in inventory.");
                resMsg += item.getName() + "\n";
            }
        }
        return resMsg;
    }

    private String pickUp(GameEntity obj) {
        String resMsg = "";
        String objName = obj.getName();
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

    private static String exeCustomAction(GameAction action) {
        return "";
    }
}
