package com.entisy.gameengine.components;

import com.entisy.gameengine.Window;
import com.entisy.gameengine.util.Component;

public class SpriteRenderer extends Component {
    @Override
    public void start() {
        Window.get().getLogger().info(String.format("Loading component '%s'", getClass().getSimpleName()));
    }

    @Override
    public void update(float dt) {

    }
}
