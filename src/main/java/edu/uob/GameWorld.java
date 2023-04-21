package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GameWorld {
    public GameWorld(File entitiesFile) {

    }

    //First take in the config file and parse it
    void parseGameWorld(File configFile) {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader("config" + File.separator + "basic-entities.dot");
            parser.parse(reader);
            Graph wholeDocument = parser.getGraphs().get(0);
            ArrayList<Graph> sections = wholeDocument.getSubgraphs();
            // sections.get(0) is the locations
            ArrayList<Graph> locations = sections.get(0).getSubgraphs();
            // print out all locations
            System.out.println("All locations: ");
            for (Graph location : locations) {
                // Location name
                String locationName = location.getNodes(false).get(0).getId().getId();
                // Location description
                String locationDescription = location.getNodes(false).get(0).getAttribute("description").toString();
                System.out.println("    " + locationName + ": " + locationDescription);
                // Artefacts in this location
                // Find if there is a subgraph called "artefacts" in this location
                ArrayList<Graph> subgraphs = location.getSubgraphs();
                for (Graph subgraph : subgraphs) {
                    if (subgraph.getId().getId().equals("artefacts")) {
                        System.out.println("    Artefacts in this location: ");
//                        ArrayList<Node> artefactNodes = subgraph.getNodes(false);
//                        for (Node artefactNode : artefactNodes) {
//                            System.out.println("        checkpoint0");
//                            String artefactName = artefactNode.getId().getId();
//                            String artefactDescription = artefactNode.getAttribute("description").toString();
//                            System.out.println("        " + artefactName + ": " + artefactDescription);
//                        }
                        printArtefactsInSubgraph(subgraph);
                    }
                }
            }
            System.out.println("----------------------------------");
            // sections.get(1) is the paths
            ArrayList<Edge> paths = sections.get(1).getEdges();
            // print out all paths
            System.out.println("All paths: ");
            for (Edge path : paths) {
                System.out.println(path.getSource().getNode().getId().getId() + " -> " + path.getTarget().getNode().getId().getId());
            }
            System.out.println("----------------------------------");
            /*
            subgraph cluster001 {
                node [shape = "none"];
                cabin [description = "A log cabin in the woods"];
                subgraph artefacts {
                    node [shape = "diamond"];
                    potion [description = "A bottle of magic potion"];
                    axe [description = "A razor sharp axe"];
                    coin [description = "A silver coin"];
                }
                subgraph furniture {
                    node [shape = "hexagon"];
                    trapdoor [description = "A locked wooden trapdoor in the floor"];
                }
            }
            */

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void printArtefactsInSubgraph(Graph subgraph) {
        // print out everything in this subgraph first
        System.out.println("        <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // print out all statements in this subgraph called "artefacts"


        System.out.println("        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        ArrayList<Node> artefactNodes = subgraph.getNodes(false);
        for (Node artefactNode : artefactNodes) {
            String artefactName = artefactNode.getId().getId();
            String artefactDescription = artefactNode.getAttribute("description").toString();
            System.out.println("        " + artefactName + ": " + artefactDescription);
        }

        ArrayList<Graph> nestedSubgraphs = subgraph.getSubgraphs();
        for (Graph nestedSubgraph : nestedSubgraphs) {
            printArtefactsInSubgraph(nestedSubgraph);
        }
    }
}
