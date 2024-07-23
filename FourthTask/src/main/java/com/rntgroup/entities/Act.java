package com.rntgroup.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Act {
    private String title;
    private List<Scene> scenes;

    public Act() {
        scenes = new ArrayList<>();
    }

    public void addScene(Scene scene) {
        scenes.add(scene);
    }
}
