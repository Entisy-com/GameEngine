package com.entisy.gameengine.scene.scenes;

import com.entisy.gameengine.component.components.SpriteRenderer;
import com.entisy.gameengine.content.player.PlayerController;
import com.entisy.gameengine.scene.Scene;
import com.entisy.gameengine.util.*;

public class LevelEditorScene extends Scene {
    public LevelEditorScene() {
    }

    @Override
    public void init() {
        this.camera = new Camera(0, 0, 0);

        var player = new GameObject("Player", new Transform(100, 100, 64));
        player.addComponent(new SpriteRenderer(AssetPool.getTexture(new ResourceLocation("player"))));
        this.addGameObjectToScene(player);

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader(ResourceLocation.shader("main"));
    }

    @Override
    public void update(float dt) {
        PlayerController.get().update(150, camera, dt);

        for (var gameObject : gameObjects) {
            gameObject.update(dt);
        }

        renderer.render();
    }
}
