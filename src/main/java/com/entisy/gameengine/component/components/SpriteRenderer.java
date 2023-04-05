package com.entisy.gameengine.component.components;

import com.entisy.gameengine.component.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f color;

    public SpriteRenderer(float r, float g, float b, float a) {
        this.color = new Vector4f(r, g, b, a);
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {

    }

    public Vector4f getColor() {
        return color;
    }
}
