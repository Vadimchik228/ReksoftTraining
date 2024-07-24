package com.rntgroup;

import com.rntgroup.entities.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public class TestBase {
    public Play play;

    @BeforeEach
    public void setUp() {
        play = new Play();

        play.setAuthor("William Shakespeare");
        play.setTitle("The Tragedy of Hamlet, Prince of Denmark");
        play.addParagraph("Text placed in the public domain by Moby Lexical Tools, 1992.");
        play.addParagraph("SGML markup by Jon Bosak, 1992-1994.");

        List<Persona> personae = new ArrayList<>();
        String personaeTitle = "Dramatis Personae";
        personae.add(new Persona(personaeTitle, "CLAUDIUS, king of Denmark. ", null));
        personae.add(new Persona(personaeTitle, "VOLTIMAND", "courtiers."));
        personae.add(new Persona(personaeTitle, "A Gentleman", null));
        personae.add(new Persona(personaeTitle, "MARCELLUS", "officers."));
        personae.add(new Persona(personaeTitle, "FRANCISCO, a soldier.", null));
        play.addPersonae(personae);

        play.setSceneDescription("SCENE  Denmark.");
        play.setPlaySubTitle("HAMLET");

        Act act = new Act();
        act.setTitle("ACT I");

        Scene scene = new Scene();
        scene.setTitle("SCENE I.  Elsinore. A platform before the castle.");

        scene.addSceneAction(new StageDirection("FRANCISCO at his post. Enter to him BERNARDO"));

        Speech speech1 = new Speech();
        speech1.addSpeaker("FRANCISCO");
        speech1.addSpeaker("BERNARDO");
        speech1.addSpeechInstruction(getSimpleLine("Who's there?"));
        scene.addSceneAction(speech1);

        scene.addSceneAction(new StageDirection("Enter KING CLAUDIUS, QUEEN GERTRUDE, HAMLET,\n" +
                                                "                POLONIUS, LAERTES, VOLTIMAND, CORNELIUS, Lords,\n" +
                                                "                and Attendants"));

        Speech speech2 = new Speech();
        speech2.addSpeaker("HAMLET");
        speech2.addSpeechInstruction(getSimpleLine("'In her excellent white bosom, these, &c.'"));
        speech2.addSpeechInstruction(getSimpleLine("In the most high and palmy state of Rome,"));
        speech2.addSpeechInstruction(new StageDirection("Re-enter Ghost"));
        speech2.addSpeechInstruction(getSimpleLine("I'll cross it, though it blast me. Stay, illusion!"));
        scene.addSceneAction(speech2);

        Speech speech3 = new Speech();
        speech3.addSpeaker("HAMLET");
        Line line = new Line();
        line.setLineInstruction(new StageDirection("Aside"));
        line.setText("  A little more than kin, and less than kind.");
        speech3.addSpeechInstruction(line);
        scene.addSceneAction(speech3);

        scene.addSceneAction(new StageDirection("Exeunt"));

        act.addScene(scene);
        play.addAct(act);
    }

    private Line getSimpleLine(String text) {
        Line line = new Line();
        line.setText(text);
        return line;
    }

}