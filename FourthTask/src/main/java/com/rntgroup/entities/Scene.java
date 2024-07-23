package com.rntgroup.entities;

import com.rntgroup.entities.interfaces.SceneAction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Scene {
    private String title;
    private List<SceneAction> sceneActions;

    public Scene() {
        sceneActions = new ArrayList<>();
    }

    public void addSceneAction(SceneAction sceneAction) {
        sceneActions.add(sceneAction);
    }
}
