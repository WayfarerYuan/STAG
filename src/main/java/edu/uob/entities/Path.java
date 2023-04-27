package edu.uob.entities;

public class Path {
    private String source;
    private String destination;

    public Path(String source, String destination) {
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
