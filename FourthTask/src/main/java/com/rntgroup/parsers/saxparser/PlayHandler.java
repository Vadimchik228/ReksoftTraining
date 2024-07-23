package com.rntgroup.parsers.saxparser;

import com.rntgroup.entities.*;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static com.rntgroup.util.TagNames.*;

public class PlayHandler extends DefaultHandler {
    @Getter
    private Play play;
    private String title;
    private String personaeTitle;
    private String currentGroupDescription;
    private List<Persona> personae;
    private Act currentAct;
    private Scene currentScene;
    private Speech currentSpeech;
    private Line currentLine;
    private StageDirection currentStageDirection;
    private String currentElement;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;
        switch (qName) {
            case PLAY_TAG: {
                play = new Play();
                break;
            }

            case TITLE_TAG: {
                if (play != null && play.getAuthor() == null) {
                    play.setAuthor(attributes.getValue(TITLE_AUTHOR_ATTRIBUTE));
                }
                break;
            }

            case PERSONAE_TAG: {
                personae = new ArrayList<>();
                break;
            }

            case PERSONAE_GROUP_TAG: {
                if (play != null) play.addPersonae(personae);
                if (personae != null) personae.clear();
                break;
            }

            case ACT_TAG: {
                currentAct = new Act();
                break;
            }

            case SCENE_TAG: {
                currentScene = new Scene();
                break;
            }

            case SPEECH_TAG: {
                currentSpeech = new Speech();
                break;
            }

            case LINE_TAG: {
                currentLine = new Line();
                break;
            }

            case STAGE_DIRECTION_TAG: {
                currentStageDirection = new StageDirection();
                break;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String text = new String(ch, start, length);
        if (text.contains("<") || currentElement == null) {
            return;
        }

        switch (currentElement) {
            case TITLE_TAG: {
                title = text;
                if (play != null && play.getTitle() == null) {
                    play.setTitle(title);
                } else if (personae != null) {
                    personaeTitle = title;
                } else if (currentAct != null && currentAct.getTitle() == null) {
                    currentAct.setTitle(title);
                } else if (currentScene != null && currentScene.getTitle() == null) {
                    currentScene.setTitle(title);
                }
                break;
            }

            case P_TAG: {
                if (play != null) play.addParagraph(text);
                break;
            }

            case PERSONA_TAG: {
                if (personae != null) {
                    var persona = Persona.builder()
                            .title(personaeTitle)
                            .name(text)
                            .groupDescription(null)
                            .build();
                    personae.add(persona);
                }
                break;
            }

            case GROUP_DESCRIPTION_TAG: {
                currentGroupDescription = text;
                break;
            }

            case SCENE_DESCRIPTION_TAG: {
                if (play != null) play.setSceneDescription(text);
                break;
            }

            case PLAY_SUBTITLE_TAG: {
                if (play != null) play.setPlaySubTitle(text);
                break;
            }

            case SPEAKER_TAG: {
                if (currentSpeech != null) currentSpeech.addSpeaker(text);
                break;
            }

            case STAGE_DIRECTION_TAG: {
                currentStageDirection.setDirection(currentStageDirection.getDirection() + text);
                break;
            }

            case LINE_TAG: {
                currentLine.setText(currentLine.getText() + text);
                break;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        String newValueOfCurElem = null;
        switch (qName) {
            case TITLE_TAG: {
                title = null;
                break;
            }

            case PERSONAE_TAG: {
                if (play != null) play.addPersonae(personae);
                personae = null;
                personaeTitle = null;
                break;
            }

            case PERSONAE_GROUP_TAG: {
                if (personae != null) {
                    personae.forEach(persona -> persona.setGroupDescription(currentGroupDescription));
                }
                currentGroupDescription = null;
                break;
            }

            case ACT_TAG: {
                if (play != null) play.addAct(currentAct);
                currentAct = null;
                break;
            }

            case SCENE_TAG: {
                if (currentAct != null) currentAct.addScene(currentScene);
                currentScene = null;
                break;
            }

            case SPEECH_TAG: {
                if (currentScene != null) currentScene.addSceneAction(currentSpeech);
                currentSpeech = null;
                break;
            }

            case LINE_TAG: {
                if (currentSpeech != null) currentSpeech.addSpeechInstruction(currentLine);
                currentLine = null;
                break;
            }

            case STAGE_DIRECTION_TAG: {
                if (currentLine != null) {
                    currentLine.setLineInstruction(currentStageDirection);
                    newValueOfCurElem = LINE_TAG;
                } else if (currentSpeech != null) {
                    currentSpeech.addSpeechInstruction(currentStageDirection);
                } else if (currentScene != null) {
                    currentScene.addSceneAction(currentStageDirection);
                }
                currentStageDirection = null;
                break;
            }
        }

        currentElement = newValueOfCurElem;
    }

}
