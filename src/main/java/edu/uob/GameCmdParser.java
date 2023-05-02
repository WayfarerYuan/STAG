package edu.uob;

import edu.uob.action.component.Subject;
import edu.uob.action.component.Trigger;

import java.util.ArrayList;
import java.util.HashSet;

public class GameCmdParser {
    private GameFileReader gameFileReader;
    private GameCmdTokenizer gameCmdTokenizer;
    private ArrayList<String> cmdTokens;
    private GameAction parsedAction;
    private Trigger parsedTrigger;

    // This parser is used to take in the tokens and build up valid GameAction objects
    public GameCmdParser(GameFileReader gameFileReader, GameCmdTokenizer gameCmdTokenizer) {
        this.gameFileReader = gameFileReader;
        this.gameCmdTokenizer = gameCmdTokenizer;
        this.cmdTokens = gameCmdTokenizer.getCmdTokens();
    }

    public GameAction parse() throws IllegalArgumentException{
        try {
            parsedTrigger = findTrigger(cmdTokens);
//            parsedAction = new GameAction(parsedTrigger.getName());
//            parsedAction.setTrigger(parsedTrigger);
            HashSet<GameAction> loadedAction = loadGameActions();
            // if loadedActions is empty, or contains more than one GameAction, throw an exception
            if (loadedAction.size() == 0) {
                throw new IllegalArgumentException("[Parser] No GameAction was loaded");
            } else if (loadedAction.size() > 1) {
                throw new IllegalArgumentException("[Parser] More than one matched GameActions found");
            }
            parsedAction = loadedAction.iterator().next();
            parsedAction.setTrigger(parsedTrigger);
            System.out.println("[Parser] parse(): " + parsedAction.getName());
            System.out.println("    [Parser] parse(): trigger ->" + parsedAction.getTrigger().getName());
            ArrayList<GameEntity> subjects = parsedAction.getSubjects();
            for (GameEntity subject : subjects) {
                System.out.println("    [Parser] parse(): subjects ->" + subject.getName());
            }
            ArrayList<GameEntity> consumed = parsedAction.getConsumed();
            for (GameEntity consume : consumed) {
                System.out.println("    [Parser] parse(): consumed ->" + consume.getName());
            }
            ArrayList<GameEntity> produced = parsedAction.getProduced();
            for (GameEntity produce : produced) {
                System.out.println("    [Parser] parse(): produced ->" + produce.getName());
            }
            System.out.println("    [Parser] parse(): narration ->" + parsedAction.getNarration().getName());

//        parsedAction.setUserName(cmdTokens.get(0));
//        parsedAction.setTrigger(findTrigger(cmdTokens));
//        parsedAction.setTarget(findTarget(cmdTokens));
//        parsedAction.setConsumed(findConsumed(cmdTokens));
//        parsedAction.setProduced(findProduced(cmdTokens));
            return parsedAction;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[Parser] " + e.getMessage());
        }
    }

    public Trigger findTrigger(ArrayList<String> cmdTokens) throws IllegalArgumentException{
        ArrayList<Trigger> foundTriggers = new ArrayList<>();
        //ArrayList<String> triggers = gameFileReader.getAllKeyPhrases();
        ArrayList<String> keyPhrases = gameCmdTokenizer.getReservedPhrases();
        for (String token : cmdTokens) {
            for (String keyPhrase : keyPhrases) {
                if (token.equals(keyPhrase)) {
                    Trigger newTrigger = new Trigger(keyPhrase);
                    foundTriggers.add(newTrigger);
                    System.out.println("[Parser] Trigger found: " + newTrigger.getName());
                }
            }
        }
        switch (foundTriggers.size()) {
            case 0:
                throw new IllegalArgumentException("[WARNING] No trigger found");
            case 1:
                return foundTriggers.get(0);
            default:
                throw new IllegalArgumentException("[WARNING] Unsupported number of triggers or built-in commands found");
        }
    }

    public HashSet<GameAction> loadGameActions() throws IllegalArgumentException{
        try {
            HashSet<GameAction> gameActions = new HashSet<>();
            System.out.println("[Parser] Loading GameActions for trigger: " + parsedTrigger.getName());
            if (gameFileReader.getActions().containsKey(parsedTrigger.getName())) {
                gameActions = gameFileReader.getActions().get(parsedTrigger.getName());
                //print out the loaded GameActions
                for (GameAction gameAction : gameActions) {
                    System.out.println("[Parser] Loaded GameAction: " + gameAction.getName());
                }
            } else {
                // print out all the contained Keys in the HashMap
                System.out.println("[Parser] Available triggers: ");
                for (String key : gameFileReader.getActions().keySet()) {
                    System.out.println("[Parser] " + key);
                }
                throw new IllegalArgumentException("[Parser] No GameAction found for trigger: " + parsedTrigger.getName());
            }
            return gameActions;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[Parser] " + e.getMessage());
        }
    }
}
