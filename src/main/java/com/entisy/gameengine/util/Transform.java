package com.entisy.gameengine.util;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {
    public Vector2f scale;
    public Vector3f position;

    public Transform() {
        this(0, 0, 0, 0);
    }

    public Transform(float posX, float posY) {
        this(posX, posY, 0, 0);
    }

    public Transform(float posX, float posY, float scale) {
        this(posX, posY, scale, scale);
    }

    public Transform(float posX, float posY, float scaleX, float scaleY) {
        this.init(posX, posY, scaleX, scaleY);
    }

    public void init(float posX, float posY, float scaleX, float scaleY) {
        this.scale = new Vector2f(scaleX, scaleY);
        this.position = new Vector3f(posX, posY, 0);
    }
}
