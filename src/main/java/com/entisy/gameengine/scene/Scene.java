package com.entisy.gameengine.scene;

import com.entisy.gameengine.render.Renderer;
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
        return this.camera;
    }

    public void start() {
        for (var gameObject : this.gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        this.isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject) {
        if (!this.isRunning) {
            this.gameObjects.add(gameObject);
        } else {
            this.gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public abstract void init();
    public abstract void update(float dt);
}
