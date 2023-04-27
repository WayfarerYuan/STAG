package edu.uob;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ActionsFileTests {

  // Test to make sure that the basic actions file is readable
  @Test
  void testBasicActionsFileIsReadable() {
      try {
          DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
          Document document = builder.parse("config" + File.separator + "basic-actions.xml");
          Element root = document.getDocumentElement();
          NodeList actions = root.getChildNodes();
          // Get the first action (only the odd items are actually actions - 1, 3, 5 etc.)
          Element firstAction = (Element)actions.item(1);
          Element triggers = (Element)firstAction.getElementsByTagName("triggers").item(0);
          // Get the first trigger phrase
          String firstTriggerPhrase = triggers.getElementsByTagName("keyphrase").item(0).getTextContent();
          assertEquals("open", firstTriggerPhrase, "First trigger phrase was not 'open'");
      } catch(ParserConfigurationException pce) {
          fail("ParserConfigurationException was thrown when attempting to read basic actions file");
      } catch(SAXException saxe) {
          fail("SAXException was thrown when attempting to read basic actions file");
      } catch(IOException ioe) {
          fail("IOException was thrown when attempting to read basic actions file");
      }
  }

    @Test
    void testBasicActionsFileIsReadable2() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse("config" + File.separator + "basic-actions.xml");
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            // Get the first action (only the odd items are actually actions - 1, 3, 5 etc.)
            Element firstAction = (Element)actions.item(3);
            // just for curiosity: print out all the action.items
//             for (int i = 0; i < actions.getLength(); i++) {
//                 Element theAction = (Element)actions.item(i);
//                 Element triggers = (Element)theAction.getElementsByTagName("triggers").item(0);
//                 String firstTriggerPhrase = triggers.getElementsByTagName("keyphrase").item(0).getTextContent();
//                    System.out.println("Action " + i + " is " + firstTriggerPhrase);
//             }
            Element triggers = (Element)firstAction.getElementsByTagName("triggers").item(0);
            // Get the first trigger phrase
            String firstTriggerPhrase = triggers.getElementsByTagName("keyphrase").item(0).getTextContent();
            assertEquals("chop", firstTriggerPhrase, "Second trigger phrase was not 'chop'");
        } catch(ParserConfigurationException pce) {
            fail("ParserConfigurationException was thrown when attempting to read basic actions file");
        } catch(SAXException saxe) {
            fail("SAXException was thrown when attempting to read basic actions file");
        } catch(IOException ioe) {
            fail("IOException was thrown when attempting to read basic actions file");
        }
    }

}
