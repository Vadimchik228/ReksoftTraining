package com.rntgroup.entities;

import com.rntgroup.entities.interfaces.LineInstruction;
import com.rntgroup.entities.interfaces.SceneAction;
import com.rntgroup.entities.interfaces.SpeechInstruction;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StageDirection implements SceneAction, SpeechInstruction, LineInstruction {
    private String direction;

    public StageDirection() {
        direction = "";
    }
}
