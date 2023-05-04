package edu.uob;

import edu.uob.action.component.ActionEntity;
import edu.uob.action.component.Trigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class GameCmdParser {
    private final GameFileReader gameFileReader;
    private final GameCmdTokenizer gameCmdTokenizer;
    private final ArrayList<String> cmdTokens;
    private Trigger parsedTrigger;
    public boolean isGrammarOK = false;

    // This parser is used to take in the tokens and build up valid GameAction objects
    public GameCmdParser(GameFileReader gameFileReader, GameCmdTokenizer gameCmdTokenizer) {
        this.gameFileReader = gameFileReader;
        this.gameCmdTokenizer = gameCmdTokenizer;
        this.cmdTokens = gameCmdTokenizer.getCmdTokens();
    }

    public GameAction parse() throws IllegalArgumentException{
        try {
            // debug
            System.out.println("[Parser] Parsing.parse() get cmd: " + cmdTokens.toString());
            parsedTrigger = findTrigger(cmdTokens);
            HashSet<GameAction> loadedAction = loadGameActions();
            // if loadedActions is empty, or contains more than one GameAction, throw an exception
            if (loadedAction.size() == 0) {
                throw new IllegalArgumentException("No GameAction was loaded");
            } else if (loadedAction.size() > 1) {
                throw new IllegalArgumentException("More than one matched GameActions found");
            }
            GameAction parsedAction = loadedAction.iterator().next();
            parsedAction.setTrigger(parsedTrigger);
            isGrammarOK = checkCmdForGameEntities(cmdTokens, parsedAction);
            return parsedAction;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[Parser] " + e.getMessage());
        }
    }

    public Trigger findTrigger(ArrayList<String> cmdTokens) throws IllegalArgumentException{
        ArrayList<Trigger> foundTriggers = new ArrayList<>();
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
        return switch (foundTriggers.size()) {
            case 0 -> throw new IllegalArgumentException("No trigger found");
            case 1 -> foundTriggers.get(0);
            default ->
                    throw new IllegalArgumentException("Unsupported number of triggers or built-in commands found");
        };
    }

    public HashSet<GameAction> loadGameActions() throws IllegalArgumentException{
        try {
            HashSet<GameAction> gameActions;
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
                throw new IllegalArgumentException("No GameAction found for trigger: " + parsedTrigger.getName());
            }
            return gameActions;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[Parser] " + e.getMessage());
        }
    }

    public boolean checkCmdForGameEntities(ArrayList<String> cmdTokens, GameAction gameAction) throws IllegalArgumentException{
        try {
            if (gameAction.isBuiltIn) {
                switch (gameAction.getName()) {
                    case "inventory", "look" -> {
                        if (cmdTokens.size() == 1) {
                            return true;
                        } else {
                            throw new IllegalArgumentException("[Parser] Too many arguments for this built-in command");
                            //return false;
                        }
                    }
                    case "get", "drop", "goto" -> {
                        if (cmdTokens.size() == 2) {
                            GameEntity targetEntity = new ActionEntity(cmdTokens.get(1), null);
                            gameAction.getSubjects().clear();
                            gameAction.addSubject(targetEntity);
                            return true;
                        } else {
                            throw new IllegalArgumentException("Too few or Too many arguments for this built-in command");
                            //return false;
                        }
                    }
                }
            }
            return checkCmdForEntities(cmdTokens, gameAction.getSubjects(), gameAction::setFoundRelatedSubjects, "subjects", gameAction.isBuiltIn) &&
                    checkCmdForEntities(cmdTokens, gameAction.getConsumed(), gameAction::setFoundRelatedConsumed, "consumed", gameAction.isBuiltIn) &&
                    checkCmdForEntities(cmdTokens, gameAction.getProduced(), gameAction::setFoundRelatedProduced, "produced", gameAction.isBuiltIn);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[Parser] " + e.getMessage());
        }
    }

    private boolean checkCmdForEntities(ArrayList<String> cmdTokens, ArrayList<GameEntity> requiredEntities,
                                        Consumer<ArrayList<GameEntity>> setFoundEntities, String entityType, Boolean isBuiltIn) throws IllegalArgumentException{
        ArrayList<GameEntity> foundEntities = new ArrayList<>();
        for (String token : cmdTokens) {
            for (GameEntity requiredEntity : requiredEntities) {
                if (token.equals(requiredEntity.getName())) {
                    foundEntities.add(requiredEntity);
                }
            }
        }

        int minNumOfEntities;
        int minNumOfSubjects = 1;
        int minNumOfConsumed = 0;
        int minNumOfProduced = 0;
        minNumOfEntities = switch (entityType) {
            case "subjects" -> minNumOfSubjects;
            case "consumed" -> minNumOfConsumed;
            case "produced" -> minNumOfProduced;
            default -> throw new IllegalArgumentException("[Parser] Invalid entity type");
        };
        if (isBuiltIn) {
            minNumOfEntities = 0;
        }

        if (foundEntities.size() < minNumOfEntities) {
            throw new IllegalArgumentException("[Parser] Not enough " + entityType + " found");
            //return false;
        } else {
            setFoundEntities.accept(foundEntities);
            return true;
        }
    }

}
