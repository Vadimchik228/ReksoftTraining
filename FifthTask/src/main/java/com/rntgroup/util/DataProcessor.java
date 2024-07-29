package com.rntgroup.util;

import com.rntgroup.entities.interfaces.Processable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataProcessor {
    public static void processAll(Iterable<Processable> processables) {
        for (Processable processable : processables) {
            processable.process();
        }
    }
}