package com.rntgroup.parsers.domparser;

import com.rntgroup.entities.*;
import com.rntgroup.entities.interfaces.SpeechInstruction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.rntgroup.util.TagNames.*;

public class PlayDomParser {
    private final DocumentBuilder builder;

    public PlayDomParser() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
    }

    public Play parse(File file) throws IOException, SAXException {
        Document document = builder.parse(file);

        Play play = new Play();
        Element playElement = document.getDocumentElement();
        parseTitleAndAuthor(playElement, play);
        parseParagraphs(playElement, play);
        parsePersonae(playElement, play);
        parseSceneDescription(playElement, play);
        parsePlaySubtitle(playElement, play);
        parseActs(playElement, play);

        return play;
    }

    private void parseTitleAndAuthor(Element playElement, Play play) {
        Element titleElement = (Element) playElement.getElementsByTagName(TITLE_TAG).item(0);
        if (titleElement != null) {
            play.setTitle(titleElement.getTextContent());
            if (titleElement.hasAttribute(TITLE_AUTHOR_ATTRIBUTE)) {
                play.setAuthor(titleElement.getAttribute(TITLE_AUTHOR_ATTRIBUTE));
            }
        }
    }

    private void parseParagraphs(Element playElement, Play play) {
        Element fmElement = (Element) playElement.getElementsByTagName(FM_TAG).item(0);
        if (fmElement != null) {
            NodeList paragraphElements = fmElement.getElementsByTagName(P_TAG);
            for (int i = 0; i < paragraphElements.getLength(); i++) {
                if (paragraphElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element paragraphElement = (Element) paragraphElements.item(i);
                    play.addParagraph(paragraphElement.getTextContent());
                }
            }
        }
    }

    private void parsePersonae(Element playElement, Play play) {
        Element personaeElement = (Element) playElement.getElementsByTagName(PERSONAE_TAG).item(0);

        if (personaeElement != null) {
            NodeList personaElements = personaeElement.getElementsByTagName(PERSONA_TAG);
            List<Persona> personae = new ArrayList<>();
            Element personaeTitleElement = (Element) personaeElement.getElementsByTagName(TITLE_TAG)
                    .item(0);

            for (int i = 0; i < personaElements.getLength(); i++) {
                if (personaElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element personaElement = (Element) personaElements.item(i);
                    Persona persona = Persona.builder()
                            .title(personaeTitleElement != null ? personaeTitleElement.getTextContent() : "")
                            .name(personaElement.getTextContent())
                            .build();
                    parseGroupDescription(personaElement, persona);
                    personae.add(persona);
                }
            }

            play.addPersonae(personae);
        }
    }

    private void parseGroupDescription(Element personaElement, Persona persona) {
        Node parentNode = personaElement.getParentNode();

        if (parentNode.getNodeName().equals(PERSONAE_GROUP_TAG)) {
            NodeList groupDescriptionElements = parentNode.getChildNodes();
            for (int j = 0; j < groupDescriptionElements.getLength(); j++) {
                Node childNode = groupDescriptionElements.item(j);
                if (childNode.getNodeType() == Node.ELEMENT_NODE &&
                    childNode.getNodeName().equals(GROUP_DESCRIPTION_TAG)) {
                    Element groupDescriptionElement = (Element) childNode;
                    persona.setGroupDescription(groupDescriptionElement.getTextContent());
                    break;
                }
            }
        }
    }

    private void parseSceneDescription(Element playElement, Play play) {
        Element sceneDescriptionElement = (Element) playElement.getElementsByTagName(SCENE_DESCRIPTION_TAG)
                .item(0);
        if (sceneDescriptionElement != null) {
            play.setSceneDescription(sceneDescriptionElement.getTextContent());
        }
    }

    private void parsePlaySubtitle(Element playElement, Play play) {
        Element playSubTitleElement = (Element) playElement.getElementsByTagName(PLAY_SUBTITLE_TAG)
                .item(0);
        if (playSubTitleElement != null) {
            play.setPlaySubTitle(playSubTitleElement.getTextContent());
        }
    }

    private void parseActs(Element playElement, Play play) {
        NodeList actElements = playElement.getElementsByTagName(ACT_TAG);
        for (int i = 0; i < actElements.getLength(); i++) {
            if (actElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element actElement = (Element) actElements.item(i);
                Act act = new Act();
                Element actTitleElement = (Element) actElement.getElementsByTagName(TITLE_TAG)
                        .item(0);
                if (actTitleElement != null) act.setTitle(actTitleElement.getTextContent());
                parseScenes(actElement, act);
                play.addAct(act);
            }
        }
    }

    private void parseScenes(Element actElement, Act act) {
        NodeList sceneElements = actElement.getElementsByTagName(SCENE_TAG);
        for (int j = 0; j < sceneElements.getLength(); j++) {
            if (sceneElements.item(j).getNodeType() == Node.ELEMENT_NODE) {
                Element sceneElement = (Element) sceneElements.item(j);
                Scene scene = new Scene();
                Element sceneTitleElement = (Element) sceneElement.getElementsByTagName(TITLE_TAG)
                        .item(0);
                if (sceneTitleElement != null) scene.setTitle(sceneTitleElement.getTextContent());
                parseSceneActions(sceneElement, scene);
                act.addScene(scene);
            }
        }
    }

    private void parseSceneActions(Element sceneElement, Scene scene) {
        NodeList sceneActionElements = sceneElement.getChildNodes();
        for (int i = 0; i < sceneActionElements.getLength(); i++) {
            if (sceneActionElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element sceneActionElement = (Element) sceneActionElements.item(i);

                switch (sceneActionElement.getNodeName()) {
                    case STAGE_DIRECTION_TAG: {
                        scene.addSceneAction(new StageDirection(sceneActionElement.getTextContent()));
                        break;
                    }

                    case SPEECH_TAG: {
                        Speech speech = new Speech();
                        speech.setSpeakers(parseSpeakers(sceneActionElement));
                        speech.setSpeechInstructions(parseSpeechInstructions(sceneActionElement));
                        scene.addSceneAction(speech);
                        break;
                    }
                }
            }
        }
    }

    private List<String> parseSpeakers(Element speechElement) {
        NodeList speakerElements = speechElement.getElementsByTagName(SPEAKER_TAG);
        List<String> speakers = new ArrayList<>();
        for (int j = 0; j < speakerElements.getLength(); j++) {
            speakers.add(speakerElements.item(j).getTextContent());
        }
        return speakers;
    }

    private List<SpeechInstruction> parseSpeechInstructions(Element speechElement) {
        List<SpeechInstruction> instructions = new ArrayList<>();
        NodeList speechChildren = speechElement.getChildNodes();
        for (int j = 0; j < speechChildren.getLength(); j++) {
            if (speechChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) speechChildren.item(j);
                SpeechInstruction instruction = parseSpeechInstruction(childElement);
                if (instruction != null) {
                    instructions.add(instruction);
                }
            }
        }
        return instructions;
    }

    private SpeechInstruction parseSpeechInstruction(Element childElement) {
        if (childElement.getNodeName().equals(STAGE_DIRECTION_TAG)) {
            return new StageDirection(childElement.getTextContent());
        } else if (childElement.getNodeName().equals(LINE_TAG)) {
            return parseLine(childElement);
        }
        return null;
    }

    private Line parseLine(Element lineElement) {
        Line line = new Line();
        line.setText(parseLineText(lineElement));
        line.setLineInstruction(parseNestedStageDirection(lineElement));
        return line;
    }

    private String parseLineText(Element lineElement) {
        NodeList lineChildren = lineElement.getChildNodes();
        StringBuilder lineText = new StringBuilder();
        for (int i = 0; i < lineChildren.getLength(); i++) {
            if (lineChildren.item(i).getNodeType() == Node.TEXT_NODE) {
                lineText.append(lineChildren.item(i).getNodeValue());
            }
        }
        return lineText.toString();
    }

    private StageDirection parseNestedStageDirection(Element lineElement) {
        NodeList lineChildren = lineElement.getChildNodes();
        for (int i = 0; i < lineChildren.getLength(); i++) {
            if (lineChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element nestedElement = (Element) lineChildren.item(i);
                if (nestedElement.getNodeName().equals(STAGE_DIRECTION_TAG)) {
                    return new StageDirection(nestedElement.getTextContent());
                }
            }
        }
        return null;
    }

}
