package com.entisy.gameengine.util;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {
    public Vector2f scale;
    public Vector3f position;

    public Transform() {
        init(0, 0, 0, 0);
    }

    public Transform(float posX, float posY) {
        init(posX, posY, 0, 0);
    }

    public Transform(float posX, float posY, float scaleX, float scaleY) {
        init(posX, posY, scaleX, scaleY);
    }

    public void init(float posX, float posY, float scaleX, float scaleY) {
        this.scale = new Vector2f(scaleX, scaleY);
        this.position = new Vector3f(posX, posY, 0);
    }
}
