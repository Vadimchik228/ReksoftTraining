package com.rntgroup.entities;

import com.rntgroup.entities.interfaces.LineInstruction;
import com.rntgroup.entities.interfaces.SpeechInstruction;
import lombok.Data;

@Data
public class Line implements SpeechInstruction {
    private String text;
    private LineInstruction lineInstruction;

    public Line() {
        text = "";
    }
}
