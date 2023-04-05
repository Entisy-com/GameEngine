package com.entisy.gameengine.scene.scenes;

import com.entisy.gameengine.component.components.SpriteRenderer;
import com.entisy.gameengine.scene.Scene;
import com.entisy.gameengine.content.player.PlayerController;
import com.entisy.gameengine.util.Camera;
import com.entisy.gameengine.util.GameObject;
import com.entisy.gameengine.util.Transform;

public class LevelEditorScene extends Scene {
    public LevelEditorScene() {
    }

    @Override
    public void init() {
        camera = new Camera(0, 0, 0);
        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100f;
        float sizeY = totalHeight / 100f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float posX = xOffset + (x * sizeX);
                float posY = yOffset + (y * sizeY);

                var gameObject = new GameObject(String.format("Obj %s,%s", x, y), new Transform(posX, posY, sizeX, sizeY));
                gameObject.addComponent(new SpriteRenderer(posX / totalWidth, posY / totalHeight, 1, 1));
                addGameObjectToScene(gameObject);
            }
        }
    }

    @Override
    public void update(float dt) {
        for (var gameObject : gameObjects) {
            gameObject.update(dt);
        }

        renderer.render();
    }
}
