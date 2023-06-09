package edu.uob.entities;

import edu.uob.GameEntity;

import java.util.ArrayList;

public class Location extends GameEntity {
    //    locations are complex constructs in their own right and have various different attributes including:
//    Paths to other locations (note: it is possible for paths to be one-way !)
//    Characters that are currently at a location
//    Artefacts that are currently present in a location
//    Furniture that belongs within a location
    private ArrayList<Path> paths;
    private ArrayList<Character> characters;
    private ArrayList<Artefact> artefacts;
    private ArrayList<Furniture> furnitures;

    public Location(String name, String description) {
        super(name, description);
        paths = new ArrayList<Path>();
        characters = new ArrayList<Character>();
        artefacts = new ArrayList<Artefact>();
        furnitures = new ArrayList<Furniture>();
    }
    /* ---------- Artefacts ---------- */
    public ArrayList<Artefact> getArtefacts() {
        return artefacts;
    }
    public void setArtefacts(ArrayList<Artefact> newArtefacts) {
        artefacts = newArtefacts;
    }
    public void addArtefact(Artefact artefact) {
        artefacts.add(artefact);
    }
    public void removeArtefact(Artefact artefact) {
        //artefacts.remove(artefact);
        for (Artefact a : artefacts) {
            if (a.getName().equals(artefact.getName())) {
                artefacts.remove(a);
                break;
            }
        }
    }
    /* ---------- Paths ---------- */
    public ArrayList<Path> getPaths() {
        return paths;
    }
    public void setPaths(ArrayList<Path> newPaths) {
        paths = newPaths;
    }
    public void addPath(Path path) {
        paths.add(path);
    }
    public void removePath(Path path) {
        //paths.remove(path);
        for (Path p : paths) {
            if (p.getName().equals(path.getName())) {
                paths.remove(p);
                break;
            }
        }
    }
    /* ---------- Character ---------- */
    public ArrayList<Character> getCharacters() {
        return characters;
    }
    public void setCharacters(ArrayList<Character> newCharacters) {
        characters = newCharacters;
    }
    public void addCharacter(Character character) {
        characters.add(character);
    }
    public void removeCharacter(Character character) {
        //characters.remove(character);
        for (Character c : characters) {
            if (c.getName().equals(character.getName())) {
                characters.remove(c);
                break;
            }
        }
    }
    /* ---------- Furniture ---------- */
    public ArrayList<Furniture> getFurnitures() {
        return furnitures;
    }
    public void setFurnitures(ArrayList<Furniture> newFurnitures) {
        furnitures = newFurnitures;
    }
    public void addFurniture(Furniture furniture) {
        furnitures.add(furniture);
    }
    public void removeFurniture(Furniture furniture) {
        //furnitures.remove(furniture);
        for (Furniture f : furnitures) {
            if (f.getName().equals(furniture.getName())) {
                furnitures.remove(f);
                break;
            }
        }
    }
}