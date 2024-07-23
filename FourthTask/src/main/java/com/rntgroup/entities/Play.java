package com.rntgroup.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Play {
    private String author;
    private String title;
    private List<String> paragraphs;
    private List<Persona> personae;
    private String sceneDescription;
    private String playSubTitle;
    private List<Act> acts;

    public Play() {
        paragraphs = new ArrayList<>();
        personae = new ArrayList<>();
        acts = new ArrayList<>();
    }

    public void addParagraph(String paragraph) {
        paragraphs.add(paragraph);
    }

    public void addPersonae(List<Persona> newPersonae) {
        personae.addAll(newPersonae);
    }

    public void addAct(Act act) {
        acts.add(act);
    }

}
