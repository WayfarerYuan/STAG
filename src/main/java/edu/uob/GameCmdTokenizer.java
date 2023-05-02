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
    private GameWorld gameWorld;
    private String cmdUserName = "";

    public GameCmdTokenizer(GameFileReader gameFileReader, GameWorld gameWorld, String cmd) {
        this.gameFileReader = gameFileReader;
        this.gameWorld = gameWorld;
        setReservedWords();
        tokenize(cmd);
    }

    public ArrayList<String> tokenize(String cmd) {
        cmdTokens.clear();
        cmdUserName = cmd.split(":")[0];
        try {
            checkUser(cmdUserName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            if (!reservedPhrases.contains(cmd)) {
                reservedPhrases.add(cmd);
            }
        }
    }

    private void checkUser(String cmdUserName) throws Exception{
        try {
            if (cmdUserName.isEmpty() || cmdUserName.isBlank() || cmdUserName == null) {
                throw new Exception("Empty or Null user name provided");
            }
            // Check if user exists, if not, create new user
            for (GamePlayer player : gameWorld.getPlayersList()) {
                if (player.getName().equals(cmdUserName)) {
                    return;
                }
            }
            gameWorld.newPlayer(cmdUserName);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public GamePlayer getPlayerFromCmd() throws Exception{
        try {
            for (GamePlayer player : gameWorld.getPlayersList()) {
                if (player.getName().equals(cmdUserName)) {
                    return player;
                }
            }
            throw new Exception("[CmdTokenizer] No Player found from the command");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ArrayList<String> getCmdTokens() {
        return (ArrayList<String>) cmdTokens;
    }

    public ArrayList<String> getReservedPhrases() {
        return (ArrayList<String>) reservedPhrases;
    }

}
