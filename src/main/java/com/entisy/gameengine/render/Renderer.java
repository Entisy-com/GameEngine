package com.entisy.gameengine.render;

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
        var renderer = gameObject.getComponent(SpriteRenderer.class);
        if (renderer != null)
            this.add(renderer);
    }

    private void add(SpriteRenderer renderer) {
        var added = false;

        for (var batch : batches)
            if (batch.hasRoom()) {
                batch.addSprite(renderer);
                added = true;
                break;
            }

        if (!added) {
            var batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.start();
            this.batches.add(batch);
            batch.addSprite(renderer);
        }
    }

    public void render() {
        for (var batch : batches)
            batch.render();
    }
}
