package com.entisy.gameengine;

import com.entisy.gameengine.util.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {


    protected Camera camera;
    private boolean isRunning = false;
    public List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){

    };

    public void start() {
        for (var gameObject : gameObjects) {
            gameObject.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject) {
        if (!isRunning) {
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
        }
    }

    public abstract void init();
    public abstract void update(float dt);
}
