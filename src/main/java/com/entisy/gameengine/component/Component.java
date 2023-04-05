package com.entisy.gameengine.component;

import com.entisy.gameengine.util.GameObject;

public abstract class Component {
    public GameObject gameObject = null;

    public void start() {}

    public abstract void update(float dt);
}
