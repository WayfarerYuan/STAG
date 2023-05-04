package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

class STAGIntegrationTests {

    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() throws ParserConfigurationException {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }
    @Test
    void testLook() {
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
        assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("forest"), "Did not see available paths in response to look");
    }

    // Test that we can pick something up and that it appears in our inventory
    @Test
    void testGet()
    {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the room after an attempt was made to get it");
    }

    // Test that we can goto a different location (we won't get very far if we can't move around the game !)
    @Test
    void testGoto()
    {
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
    }

    // Test inv
    @Test
    void testInv()
    {
        String response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("empty"), "Inventory is not empty at the start of the game");
    }

    // Test drop
    @Test
    void testDrop()
    {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
        response = sendCommandToServer("simon: drop potion");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the room after an attempt was made to drop it");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the inventory after an attempt was made to drop it");
    }

    // Test look then goto
    @Test
    void testLookThenGoto()
    {
        String response;
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
        assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("forest"), "Did not see available paths in response to look");
        response = sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
        assertTrue(response.contains("forest"), "Did not see the name of the current room in response to look");
    }

    // Test get then inv
    @Test
    void testGetThenInv()
    {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
    }

    // Test get then drop
    @Test
    void testGetThenDrop()
    {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
        response = sendCommandToServer("simon: drop potion");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the room after an attempt was made to drop it");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the inventory after an attempt was made to drop it");
    }

    // Test get then look
    @Test
    void testGetThenLook()
    {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the room after an attempt was made to get it");
    }

    // Test get then goto
    @Test
    void testGetThenGoto()
    {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
    }

    // Test drop then inv
    @Test
    void testDropThenInv()
    {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: drop potion");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the inventory after an attempt was made to drop it");
    }

    // Test drop then look
    @Test
    void testDropThenLook()
    {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: drop potion");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the room after an attempt was made to drop it");
    }

    // Test drop then goto
    @Test
    void testDropThenGoto()
    {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: drop potion");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the inventory after an attempt was made to drop it");
    }

    // Test get non-existent item
    @Test
    void testGetNonExistentItem()
    {
        String response;
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertFalse(response.contains("key"), "Should not be able to see the key in the inventory since it does not exist in the current location");
        assertTrue(response.contains("empty"), "Should see a message indicating the inventory is empty");
    }

    // Test drop non-existent item
    @Test
    void testDropNonExistentItem()
    {
        String response;
        sendCommandToServer("simon: drop key");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertFalse(response.contains("key"), "Should not be able to see the key in the inventory since it does not exist in the current location");
        assertTrue(response.contains("empty"), "Should see a message indicating the inventory is empty");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertFalse(response.contains("key"), "Should not be able to see the key in the room since it does not exist in the current location");
    }

    // Test to get the gold
    @Test
    void testGetGold()
    {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: open the trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: Pay the elf");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut the tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge the river");
        sendCommandToServer("simon: goto clearing");
        sendCommandToServer("simon: dig the ground");
        sendCommandToServer("simon: get gold");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("gold"), "Did not see the gold in the inventory after an attempt was made to get it");
    }

    // Test health
    @Test
    void testHealth() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open the trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: Attack the elf");
        response = sendCommandToServer("simon: health");
        response = response.toLowerCase();
        assertTrue(response.contains("2"), "Health is consumed after an attack");
    }

    // Test max health
    @Test
    void testMaxHealth() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: drink potion");
        response = sendCommandToServer("simon: health");
        response = response.toLowerCase();
        assertTrue(response.contains("3"), "Health is not maxed out after drinking a potion");
    }

    // Test Multi User
    @Test
    void testMultiUser() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("sion: goto forest");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
        assertTrue(response.contains("sion"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
        sendCommandToServer("simon: get key");
        sendCommandToServer("sion: get key");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
        response = sendCommandToServer("sion: inv");
        response = response.toLowerCase();
        assertFalse(response.contains("key"), "Should not be able to get the key since it is already taken by another user");
        sendCommandToServer("simon: drop key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("sion: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Should be able to see the key in the location since it was dropped by another user");
        assertFalse(response.contains("simon"), "Should not be able to see the other user in the location since it is not in the same location");
    }

    // Test die
    @Test
    void testToDie() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: open the trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: Pay the elf");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut the tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge the river");
        sendCommandToServer("simon: goto clearing");
        sendCommandToServer("simon: dig the ground");
        sendCommandToServer("simon: get gold");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: Attack the elf");
        sendCommandToServer("simon: Attack the elf");
        response = sendCommandToServer("simon: Attack the elf");
        response = response.toLowerCase();
        assertTrue(response.contains("die"), "You should die after 3 attacks");
        response = sendCommandToServer("simon: health");
        response = response.toLowerCase();
        assertTrue(response.contains("3"), "Health should be reset to 3 after die");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("cabin -> cellar"), "The path to cellar should be still open after die");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("gold"), "The gold should be dropped in the cellar after die");
    }
}
