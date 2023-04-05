package com.entisy.gameengine.component.components;

import com.entisy.gameengine.Window;
import com.entisy.gameengine.component.Component;

public class FontRenderer extends Component {
    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            Window.get().getLogger().info("Found font renderer");
        }
    }

    @Override
    public void update(float dt) {

    }
}
