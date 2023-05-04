package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class STAGUnitTests {

    private GameCmdTokenizer gameCmdTokenizer;
    private GameFileReader gameFileReader;
    private GameWorld gameWorld;

    private GameCmdParser gameCmdParser;

    @BeforeEach
    public void setUp() throws Exception {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        gameFileReader = new GameFileReader(entitiesFile, actionsFile);
        gameWorld = new GameWorld(gameFileReader);
    }

    @Test
    public void testTokenize() throws Exception {
        String command = "player1:go to Cellar";
        gameCmdTokenizer = new GameCmdTokenizer(gameFileReader, gameWorld, command);
        ArrayList<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("go");
        expectedTokens.add("to");
        expectedTokens.add("Cellar");

        assertEquals(expectedTokens, gameCmdTokenizer.getCmdTokens());
    }

    @Test
    public void testReservedPhrases() throws Exception{
        String command = "player1:go to Cellar";
        gameCmdTokenizer = new GameCmdTokenizer(gameFileReader, gameWorld, command);
        ArrayList<String> reservedPhrases = gameCmdTokenizer.getReservedPhrases();

        assertTrue(reservedPhrases.contains("get"));
        assertTrue(reservedPhrases.contains("drop"));
        assertTrue(reservedPhrases.contains("goto"));
        assertTrue(reservedPhrases.contains("look"));
        assertTrue(reservedPhrases.contains("inventory"));
        assertTrue(reservedPhrases.contains("inv"));
        assertTrue(reservedPhrases.contains("health"));
    }

    @Test
    public void testGetPlayerFromCmd() throws Exception {
        String command = "player1:go to Cellar";
        gameCmdTokenizer = new GameCmdTokenizer(gameFileReader, gameWorld, command);
        GamePlayer player = gameCmdTokenizer.getPlayerFromCmd();

        assertEquals("player1", player.getName());
    }

    @Test
    void testParseValidCommand() throws Exception{
        // Initialize gameCmdTokenizer with a valid command
        String command = "player1:go to Cellar";
        gameCmdTokenizer = new GameCmdTokenizer(gameFileReader, gameWorld, command);
        gameCmdTokenizer.tokenize(command);

        gameCmdParser = new GameCmdParser(gameFileReader, gameCmdTokenizer);

        try {
            GameAction parsedAction = gameCmdParser.parse();
            assertNotNull(parsedAction, "Parsed action should not be null");
            // Add more assertions based on expected values of the parsed action
        } catch (IllegalArgumentException e) {
            fail("Exception should not be thrown for a valid command");
        }
    }

    @Test
    void testParseInvalidCommand() throws Exception{
        // Initialize gameCmdTokenizer with an invalid command
        String command = "sample_invalid_command";
        gameCmdTokenizer = new GameCmdTokenizer(gameFileReader, gameWorld, command);
        gameCmdTokenizer.tokenize(command);

        gameCmdParser = new GameCmdParser(gameFileReader, gameCmdTokenizer);

        assertThrows(IllegalArgumentException.class, () -> gameCmdParser.parse(), "IllegalArgumentException should be thrown for an invalid command");
    }

}
