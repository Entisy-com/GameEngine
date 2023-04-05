package com.entisy.gameengine.renderer;

import com.entisy.gameengine.component.components.SpriteRenderer;
import com.entisy.gameengine.util.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer renderer = gameObject.getComponent(SpriteRenderer.class);
        if (renderer != null)
            add(renderer);
    }

    private void add(SpriteRenderer renderer) {
        boolean added = false;

        for (RenderBatch batch : batches)
            if (batch.hasRoom()) {
                batch.addSprite(renderer);
                added = true;
                break;
            }

        if (!added) {
            var batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.start();
            batches.add(batch);
            batch.addSprite(renderer);
        }
    }

    public void render() {
        for (RenderBatch batch : batches)
            batch.render();
    }
}
