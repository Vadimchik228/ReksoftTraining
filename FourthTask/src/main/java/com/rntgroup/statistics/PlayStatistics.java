package com.rntgroup.statistics;

import com.rntgroup.entities.Line;
import com.rntgroup.entities.Play;
import com.rntgroup.entities.Speech;
import com.rntgroup.util.WordUtil;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.stream.Collectors;

@UtilityClass
public class PlayStatistics {
    public static int countUniqueHamletWords(Play play) {
        return play.getActs().stream()
                .flatMap(act -> act.getScenes().stream())
                .flatMap(scene -> scene.getSceneActions().stream())
                .filter(sceneAction -> sceneAction instanceof Speech)
                .map(sceneAction -> (Speech) sceneAction)
                .filter(speech -> speech.getSpeakers().contains("HAMLET"))
                .flatMap(speech -> speech.getSpeechInstructions().stream())
                .filter(speechInstruction -> speechInstruction instanceof Line)
                .map(speechInstruction -> (Line) speechInstruction)
                .flatMap(line -> WordUtil.splitAndCleanWord(line.getText()))
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(HashSet::new))
                .size();
    }
}
