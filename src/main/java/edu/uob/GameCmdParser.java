package edu.uob;

import edu.uob.action.component.ActionEntity;
import edu.uob.action.component.Subject;
import edu.uob.action.component.Trigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class GameCmdParser {
    private final GameFileReader gameFileReader;
    private final GameCmdTokenizer gameCmdTokenizer;
    private final ArrayList<String> cmdTokens;
    private GameAction parsedAction;
    private Trigger parsedTrigger;
    private int minNumOfSubjects = 1;
    private int minNumOfConsumed = 0;
    private int minNumOfProduced = 0;
    public boolean isGrammarOK = false;

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
            isGrammarOK = checkCmdForGameEntities(cmdTokens, parsedAction);
//            System.out.println("[Parser] parse(): " + parsedAction.getName());
//            System.out.println("    [Parser] parse(): trigger ->" + parsedAction.getTrigger().getName());
//            ArrayList<GameEntity> subjects = parsedAction.getSubjects();
//            for (GameEntity subject : subjects) {
//                System.out.println("    [Parser] parse(): subjects ->" + subject.getName());
//            }
//            ArrayList<GameEntity> consumed = parsedAction.getConsumed();
//            for (GameEntity consume : consumed) {
//                System.out.println("    [Parser] parse(): consumed ->" + consume.getName());
//            }
//            ArrayList<GameEntity> produced = parsedAction.getProduced();
//            for (GameEntity produce : produced) {
//                System.out.println("    [Parser] parse(): produced ->" + produce.getName());
//            }
//            System.out.println("    [Parser] parse(): narration ->" + parsedAction.getNarration().getName());
            // Based on the trigger, find the corresponding GameAction, and check if the subjects, consumed, produced exist in the cmdTokens
            // If they do, then add them to the GameAction object


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

    // Check the cmdTokens based on the trigger and return the subjects
//    public boolean checkCmdForGameEntities(ArrayList<String> cmdTokens, GameAction gameAction) throws IllegalArgumentException{
//        try {
//            boolean enoughSubjects = checkCmdForSubjects(cmdTokens, gameAction);
//            boolean enoughConsumed = checkCmdForConsumed(cmdTokens, gameAction);
//            boolean enoughProduced = checkCmdForProduced(cmdTokens, gameAction);
//            if (enoughSubjects && enoughConsumed && enoughProduced) {
//                return true;
//            } else {
//                IllegalArgumentException e = new IllegalArgumentException("[Parser] Not enough subjects, consumed, or produced found");
//                return false;
//            }
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("[Parser] " + e.getMessage());
//        }
//    }
//
//    private boolean checkCmdForSubjects(ArrayList<String> cmdTokens, GameAction gameAction) throws IllegalArgumentException{
//        try {
//            ArrayList<GameEntity> requiredSubjects = gameAction.getSubjects();
//            ArrayList<GameEntity> foundSubjects = new ArrayList<>();
//            for (String token : cmdTokens) {
//                for (GameEntity requiredSubject : requiredSubjects) {
//                    if (token.equals(requiredSubject.getName())) {
//                        foundSubjects.add(requiredSubject);
//                    }
//                }
//            }
//            if (foundSubjects.size() < minNumOfSubjects) {
//                throw new IllegalArgumentException("[Parser] Not enough subjects found");
//            } else {
//                gameAction.setFoundRelatedSubjects(foundSubjects);
//                return true;
//            }
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("[Parser] " + e.getMessage());
//        }
//    }
//
//    private boolean checkCmdForConsumed(ArrayList<String> cmdTokens, GameAction gameAction) throws IllegalArgumentException{
//        try {
//            ArrayList<GameEntity> requiredConsumed = gameAction.getConsumed();
//            ArrayList<GameEntity> foundConsumed = new ArrayList<>();
//            for (String token : cmdTokens) {
//                for (GameEntity requiredConsume : requiredConsumed) {
//                    if (token.equals(requiredConsume.getName())) {
//                        foundConsumed.add(requiredConsume);
//                    }
//                }
//            }
//            if (foundConsumed.size() < minNumOfConsumed) {
//                throw new IllegalArgumentException("[Parser] Not enough consumed found");
//            } else {
//                gameAction.setFoundRelatedConsumed(foundConsumed);
//                return true;
//            }
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("[Parser] " + e.getMessage());
//        }
//    }
//
//    private boolean checkCmdForProduced(ArrayList<String> cmdTokens, GameAction gameAction) throws IllegalArgumentException{
//        try {
//            ArrayList<GameEntity> requiredProduced = gameAction.getProduced();
//            ArrayList<GameEntity> foundProduced = new ArrayList<>();
//            for (String token : cmdTokens) {
//                for (GameEntity requiredProduce : requiredProduced) {
//                    if (token.equals(requiredProduce.getName())) {
//                        foundProduced.add(requiredProduce);
//                    }
//                }
//            }
//            if (foundProduced.size() < minNumOfProduced) {
//                throw new IllegalArgumentException("[Parser] Not enough produced found");
//            } else {
//                gameAction.setFoundRelatedProduced(foundProduced);
//                return true;
//            }
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("[Parser] " + e.getMessage());
//        }
//    }
    public boolean checkCmdForGameEntities(ArrayList<String> cmdTokens, GameAction gameAction) throws IllegalArgumentException{
        try {
            if (gameAction.isBuiltIn) {
                switch (gameAction.getName()) {
                    case "inventory", "look":
                        if (cmdTokens.size() ==1) { return true; }
                        else {
                            throw new IllegalArgumentException("[Parser] Too many arguments for this built-in command");
                            //return false;
                        }
                    case "get", "drop", "goto":
                        if (cmdTokens.size() ==2) {
                            GameEntity targetEntity = new ActionEntity(cmdTokens.get(1),null);
                            gameAction.addSubject(targetEntity);
                            return true;
                        }
                        else {
                            throw new IllegalArgumentException("[Parser] Too few or Too many arguments for this built-in command");
                            //return false;
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
        switch (entityType) {
            case "subjects":
                minNumOfEntities = minNumOfSubjects;
                break;
            case "consumed":
                minNumOfEntities = minNumOfConsumed;
                break;
            case "produced":
                minNumOfEntities = minNumOfProduced;
                break;
            default:
                throw new IllegalArgumentException("[Parser] Invalid entity type");
        }
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
