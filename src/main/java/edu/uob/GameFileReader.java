package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.action.component.ActionEntity;
import edu.uob.action.component.KeyPhrase;
import edu.uob.action.component.Narration;
import edu.uob.entities.*;
import edu.uob.entities.Character;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameFileReader {
    private ArrayList<Location> locations;
    private HashMap<String, HashSet<GameAction>> actions;
    private ArrayList<String> allKeyPhrases;

    private Location defaultLocation;

    public GameFileReader(File entitiesFile, File actionsFile) {
        // use a HashMap to store all the locations and actions
        locations = new ArrayList<>();
        actions = new HashMap<>();
        allKeyPhrases = new ArrayList<>();
        try {
            parseConfigFile(entitiesFile, actionsFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing config file: " + e.getMessage());
        }
    }
    void parseConfigFile(File entitiesFile, File actionsFile) throws ParserConfigurationException {
        parseEntitiesFile(entitiesFile);
        parseActionsFile(actionsFile);
    }
    //First take in the config file and parse it
    void parseEntitiesFile(File entitiesFile) {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entitiesFile);
            parser.parse(reader);
            Graph wholeDocument = parser.getGraphs().get(0);
            ArrayList<Graph> sections = wholeDocument.getSubgraphs();
            ArrayList<Graph> locationsFromFile = sections.get(0).getSubgraphs();
            //System.out.println("All locations: ");
            for (Graph location : locationsFromFile) {
                // Location name
                String locationName = location.getNodes(false).get(0).getId().getId();
                // Location description
                String locationDescription = location.getNodes(false).get(0).getAttribute("description");
                Location newLocation = new Location(locationName, locationDescription);
                //System.out.println("\n    set New Location - " + newLocation.getName() + ": " + newLocation.getDescription());
                ArrayList<Graph> subgraphs = location.getSubgraphs();
                for (Graph subgraph : subgraphs) {
                    String entityType = subgraph.getId().getId();
                    switch (entityType) {
                        case "artefacts" -> parseEntitiesHelper(subgraph, "Artefacts", newLocation);
                        case "characters" -> parseEntitiesHelper(subgraph, "Characters", newLocation);
                        case "furniture" -> parseEntitiesHelper(subgraph, "Furniture", newLocation);
                    }
                }
//                if (locations.containsKey(locationName)) {
//                    locations.get(locationName).add(newLocation);
//                } else {
//                    HashSet<Location> newLocationSet = new HashSet<>();
//                    newLocationSet.add(newLocation);
//                    locations.put(locationName, newLocationSet);
//                }
                locations.add(newLocation);
            }
            //System.out.println("----------------------------------");
            // sections.get(1) is the paths
            ArrayList<Edge> paths = sections.get(1).getEdges();
            // print out all paths
            //System.out.println("All paths: ");
            for (Edge path : paths) {
                String source = path.getSource().getNode().getId().getId();
                String destination = path.getTarget().getNode().getId().getId();
                Path newPath = new Path(source, destination);
                // add this path to the corresponding locations
                try {
//                    for (Location location : locations.get(source)) {
//                        location.addPath(newPath);
//                    }
                    for (Location location : locations) {
                        if (location.getName().equals(source)) {
                            location.addPath(newPath);
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println("    Error: " + source + " is not a valid location name.");
                }
                //System.out.println("    " + source + " -> " + destination);
            }
            defaultLocation = locations.get(0);
            //System.out.println("----------------------------------");
        } catch (FileNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseEntitiesHelper(Graph subgraph, String entityType, Location newLocation) {
        //System.out.printf("    %s in this location:%n", entityType);
        ArrayList<Node> entityNodes = subgraph.getNodes(false);
        for (Node entityNode : entityNodes) {
            String entityName = entityNode.getId().getId();
            String entityDescription = entityNode.getAttribute("description");
            switch (entityType) {
                case "Artefacts" -> {
                    Artefact newArtefact = new Artefact(entityName, entityDescription);
                    newLocation.addArtefact(newArtefact);
                    //System.out.printf("        location added artefact - %s: %s%n", newArtefact.getName(), newArtefact.getDescription());
                }
                case "Characters" -> {
                    Character newCharacter = new Character(entityName, entityDescription);
                    newLocation.addCharacter(newCharacter);
                    //System.out.printf("        location added character - %s: %s%n", newCharacter.getName(), newCharacter.getDescription());
                }
                case "Furniture" -> {
                    Furniture newFurniture = new Furniture(entityName, entityDescription);
                    newLocation.addFurniture(newFurniture);
                    //System.out.printf("        location added furniture - %s: %s%n", newFurniture.getName(), newFurniture.getDescription());
                }
            }
        }
    }

    private void parseActionsFile(File actionsFile) throws ParserConfigurationException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(actionsFile);
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            ArrayList<GameAction> newActions = new ArrayList<>();
            for (int i = 1; i < actions.getLength(); i += 2) {
                Element action = (Element)actions.item(i);
                Element triggers = (Element)action.getElementsByTagName("triggers").item(0);
                GameAction newAction = new GameAction(null);
                for (int j = 0; j < triggers.getElementsByTagName("keyphrase").getLength(); j++) {
                    String triggerPhrase = triggers.getElementsByTagName("keyphrase").item(j).getTextContent();
                    //System.out.println("Loaded keyphrase: " + triggerPhrase + " for action: " + action.getAttribute("name"));
                    allKeyPhrases.add(triggerPhrase);
                    KeyPhrase newKeyPhrase = new KeyPhrase(triggerPhrase);
                    newAction.addTrigger(newKeyPhrase);
                }
                parseActionsHelper(action, "subjects", newAction);
                parseActionsHelper(action, "consumed", newAction);
                parseActionsHelper(action, "produced", newAction);
                String narration = action.getElementsByTagName("narration").item(0).getTextContent();
                //System.out.println("    Narration: " + narration);
                // Narration newNarration = new Narration(narration);
                // newAction.setNarration(newNarration);
                newAction.setNarration(new Narration(narration));
                //System.out.println("----------------------------------");
                newAction.setName(newAction.getTriggers().get(0).getName());
                newActions.add(newAction);
            }
            addBuiltInCmds(newActions);
            this.actions = hashActions(newActions); // store all actions into the actions HashMap
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseActionsHelper(Element action, String elementType, GameAction newAction) throws NullPointerException {
        try {
            //System.out.println("    " + elementType + " in this action: ");
            Element elements = (Element)action.getElementsByTagName(elementType).item(0);
            for (int i = 0; i < elements.getElementsByTagName("entity").getLength(); i++) {
                String elementName = elements.getElementsByTagName("entity").item(i).getTextContent();
                if (elementName != null) {
                    GameEntity newEntity = new ActionEntity(elementName, null);
                    switch (elementType) {
                        case "subjects" -> newAction.addSubject(newEntity);
                        case "consumed" -> newAction.addConsumed(newEntity);
                        case "produced" -> newAction.addProduced(newEntity);
                    }
                }
                //System.out.println("        [" + elementName + "] added to [" + elementType + "]");
            }
        } catch (NullPointerException e) {
            //System.out.println("    Error: " + elementType + " is not a valid action component.");
            throw new RuntimeException("[GameFileReader] Error: " + elementType + " is not a valid action component.");
        }
    }

    private void addBuiltInCmds(ArrayList<GameAction> newActions) throws RuntimeException{
        // add built-in commands
        try {
            GameAction inventory = new GameAction("inventory");
            inventory.addTrigger(new KeyPhrase("inventory"));
            inventory.addTrigger(new KeyPhrase("inv"));
            inventory.isBuiltIn = true;
            if (!isCmdAlreadyExists(newActions, "inventory")) newActions.add(inventory);

            GameAction get = new GameAction("get");
            get.addTrigger(new KeyPhrase("get"));
            get.isBuiltIn = true;
            if (!isCmdAlreadyExists(newActions, "get")) newActions.add(get);

            GameAction drop = new GameAction("drop");
            drop.addTrigger(new KeyPhrase("drop"));
            drop.isBuiltIn = true;
            if (!isCmdAlreadyExists(newActions, "drop")) newActions.add(drop);

            GameAction goTo = new GameAction("goto");
            goTo.addTrigger(new KeyPhrase("goto"));
            goTo.isBuiltIn = true;
            if (!isCmdAlreadyExists(newActions, "goto")) newActions.add(goTo);

            GameAction look = new GameAction("look");
            look.addTrigger(new KeyPhrase("look"));
            look.isBuiltIn = true;
            if (!isCmdAlreadyExists(newActions, "look")) newActions.add(look);

            GameAction health = new GameAction("health");
            health.addTrigger(new KeyPhrase("health"));
            health.isBuiltIn = true;
            if (!isCmdAlreadyExists(newActions, "health")) newActions.add(health);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isCmdAlreadyExists(ArrayList<GameAction> newActions, String cmdName) {
        for (GameAction action : newActions) {
            if (action.getName().equals(cmdName)) {
                return true;
            }
        }
        return false;
    }

    private HashMap<String, HashSet<GameAction>> hashActions(ArrayList<GameAction> actions) {
        //HashMap<String,HashSet<GameAction>> actions = new HashMap<String, HashSet<GameAction>>();
        HashMap<String, HashSet<GameAction>> hashedActions = new HashMap<>();
        for (GameAction action : actions) {
            for (KeyPhrase trigger : action.getTriggers()) {
                String triggerName = trigger.getName();
                if (hashedActions.containsKey(triggerName)) {
                    hashedActions.get(triggerName).add(action);
                    //System.out.println("    Added action: " + action.getName() + " to trigger: " + triggerName);
                } else {
                    HashSet<GameAction> newActionSet = new HashSet<>();
                    newActionSet.add(action);
                    hashedActions.put(triggerName, newActionSet);
                    //System.out.println("    New hashset created for trigger: " + triggerName);
                }
            }
        }
        return hashedActions;
    }

    public HashMap<String, HashSet<GameAction>> getActions() {
        // Testing
//        System.out.println("[FileReader.getActions()]:");
//        for (String key : actions.keySet()) {
//            System.out.println("    " + key + ": " + actions.get(key));
//        }
        return actions;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public ArrayList<String> getAllKeyPhrases() {
        return allKeyPhrases;
    }

    public Location getStartingLocation() {
        return defaultLocation;
    }
}
