package com.entisy.gameengine.Scenes;

import com.entisy.gameengine.Scene;
import com.entisy.gameengine.Window;

public class LevelScene extends Scene {

    public LevelScene(){
        Window.get().getLogger().info("Inside Level scene");
        Window.get().r = 1.0f;
        Window.get().g = 1.0f;
        Window.get().b = 1.0f;

    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {

    }
}
