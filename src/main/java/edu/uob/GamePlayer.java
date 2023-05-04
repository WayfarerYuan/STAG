package edu.uob;

import edu.uob.entities.Artefact;
import edu.uob.entities.Character;
import edu.uob.entities.Location;

import java.util.ArrayList;

public class GamePlayer extends Character {
    private GameFileReader gameFileReader;
    private GameWorld gameWorld;
    private ArrayList<GamePlayer> players;
    private String name;
    private Location location;
    private ArrayList<GameEntity> inventory;
    private int health;
    private final int maxHealth = 3;

    public GamePlayer(GameFileReader gameFileReader, GameWorld gameWorld, String name) {
        super(name, "'Real Player'");
        this.gameFileReader = gameFileReader;
        this.gameWorld = gameWorld;
        this.name = name;
        this.inventory = new ArrayList<>();
        this.health = maxHealth;
        this.location = gameFileReader.getStartingLocation();
        System.out.println("Player " + name + " created.");
    }

    public String getName() {
        return name;
    }

//    public void setName(String newName) {
//        name = newName;
//    }

    public Location getLocation() {
        return location;
    }

    public void goToLocation(Location newLocation) {
        // delete the player from the old location
        location.removeCharacter(this);
        // move the player to the new location
        this.location = newLocation;
        // add the player to the new location
        location.addCharacter(this);
    }

    public ArrayList<GameEntity> getInventory() {
        return inventory;
    }

    public void addToInventory(GameEntity item) {
        inventory.add(item);
        // debug
        System.out.println("[Player] Added " + item.getName() + " to inventory.");
        System.out.println("[Player] Inventory:");
        for (GameEntity entity : inventory) {
            System.out.println(entity.getName());
        }
    }

    public void removeFromInventory(GameEntity item) {
        // inventory.remove(item);
        inventory.removeIf(entity -> entity.getName().equals(item.getName()));
    }

    public String getHealthStr() {
        return "Your health is " + health + " / " + maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int newHealth) {
        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        } else if (newHealth <= 0) {
            newHealth = 0;
            // TODO: game over
            die();
        }
        health = newHealth;
    }

    public void die() {
        System.out.println("You died!");
        // Drop down all the entities inside your inventory to the current location
        for (GameEntity entity : inventory) {
            location.addArtefact((Artefact) entity);
        }
        inventory.clear();
        setHealth(maxHealth);
        // Move the player to the starting location
        goToLocation(gameFileReader.getStartingLocation());
    }
}
