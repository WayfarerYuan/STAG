package edu.uob;

import edu.uob.action.component.KeyPhrase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GameCmdTokenizer {
    private List<String> cmdTokens = new ArrayList<>();
    private List<String> reservedPhrases = new ArrayList<>();
    private GameFileReader gameFileReader;
    private String cmdUserName = "";

    public GameCmdTokenizer(GameFileReader gameFileReader) {
        this.gameFileReader = gameFileReader;
        setReservedWords();
    }

    public ArrayList<String> tokenize(String cmd) {
        cmdTokens.clear();
        cmdUserName = cmd.split(":")[0];
        String input = cmd.split(":")[1].trim();
        String[] tokens = input.split(" ");
        if (tokens.length == 1) {
            cmdTokens.add(tokens[0]);
        } else {
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                if (i < tokens.length - 1) {
                    String combinedPhrase = token + " " + tokens[i + 1];
                    if (reservedPhrases.contains(combinedPhrase)) {
                        cmdTokens.add(combinedPhrase);
                        i++;
                        continue;
                    }
                }
                if (!token.isEmpty()) {
                    cmdTokens.add(token);
                }
            }
        }

        return (ArrayList<String>) cmdTokens;
    }

    private void setReservedWords() {
        reservedPhrases = gameFileReader.getAllKeyPhrases();
        String[] builtInCmds = {"inventory", "inv", "get", "drop", "goto", "look", "health"};
        for (String cmd : builtInCmds) {
            reservedPhrases.add(cmd);
        }
    }

}
