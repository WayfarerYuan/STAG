package edu.uob.entities;

import edu.uob.GameEntity;

public class Path extends GameEntity {
    private String source;
    private String destination;

    public Path(String source, String destination) {
        super(source + " to " + destination, "A path from " + source + " to " + destination + ".");
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }
}
