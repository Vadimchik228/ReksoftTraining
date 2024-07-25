package com.rntgroup.entities;

import com.rntgroup.entities.interfaces.SceneAction;
import com.rntgroup.entities.interfaces.SpeechInstruction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Speech implements SceneAction {
    private List<String> speakers;
    private List<SpeechInstruction> speechInstructions;

    public Speech() {
        speakers = new ArrayList<>();
        speechInstructions = new ArrayList<>();
    }

    public void addSpeaker(String speaker) {
        speakers.add(speaker);
    }

    public void addSpeechInstruction(SpeechInstruction speechInstruction) {
        speechInstructions.add(speechInstruction);
    }
}
