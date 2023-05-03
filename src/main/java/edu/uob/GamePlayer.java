package edu.uob;

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

    public void setName(String newName) {
        name = newName;
    }

    public Location getLocation() {
        return location;
    }

    public void goToLocation(Location newLocation) {
        this.location = newLocation;
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
        inventory.remove(item);
    }
}
