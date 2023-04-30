package edu.uob;

import edu.uob.action.component.Trigger;

import java.util.ArrayList;

public class GameCmdParser {
    GameFileReader gameFileReader;
    ArrayList<String> cmdTokens;
    GameAction parsedAction;

    // This parser is used to take in the tokens and build up valid GameAction objects
    public GameCmdParser(GameFileReader gameFileReader, ArrayList<String> cmdTokens) {
        this.gameFileReader = gameFileReader;
        this.cmdTokens = cmdTokens;
    }

    private Trigger findTrigger(ArrayList<String> cmdTokens) throws IllegalArgumentException{
        ArrayList<Trigger> foundTriggers = new ArrayList<>();
        ArrayList<String> triggers = gameFileReader.getAllKeyPhrases();
        for (String token : cmdTokens) {
            for (String trigger : triggers) {
                if (token.equals(trigger)) {
                    foundTriggers.add(new Trigger(trigger));
                    System.out.println("[Parser] Trigger found: " + trigger);
                }
            }
        }
        switch (foundTriggers.size()) {
            case 0:
                throw new IllegalArgumentException("[WARNING] No trigger found");
            case 1:
                return foundTriggers.get(0);
            default:
                throw new IllegalArgumentException("[WARNING] Unsupported number of triggers found");
        }
    }
}
