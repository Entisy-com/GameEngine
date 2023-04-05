package com.entisy.gameengine.scene;

import com.entisy.gameengine.renderer.Renderer;
import com.entisy.gameengine.util.Camera;
import com.entisy.gameengine.util.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();

    protected Camera camera;
    private boolean isRunning = false;
    public List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){

    };

    public Camera getCamera() {
        return camera;
    }

    public void start() {
        for (var gameObject : gameObjects) {
            gameObject.start();
            renderer.add(gameObject);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject) {
        if (!isRunning) {
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public abstract void init();
    public abstract void update(float dt);
}
