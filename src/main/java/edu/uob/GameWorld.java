package edu.uob;

import edu.uob.entities.Location;

import java.util.ArrayList;

public class GameWorld {
    private GameFileReader gameFileReader;
    private ArrayList<GamePlayer> players;
    private ArrayList<Location> locations;

    public GameWorld(GameFileReader gameFileReader) {
        this.players = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.gameFileReader = gameFileReader;
        buildWorld();
    }

    public void buildWorld() {
        this.locations = gameFileReader.getLocations();
        //Debug
        for (Location location : locations) {
            System.out.println(location.getName());
        }
    }

    public void newPlayer(String name) {
        GamePlayer newPlayer = new GamePlayer(gameFileReader, this, name);
        players.add(newPlayer);
        // add this player into starting location's player list
        Location startingLocation = gameFileReader.getStartingLocation();
        startingLocation.addCharacter(newPlayer);
    }

    public ArrayList<GamePlayer> getPlayersList() {
        return players;
    }
}
