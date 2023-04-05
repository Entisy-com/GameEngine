package com.entisy.gameengine.renderer;

import static org.lwjgl.opengl.GL30.*;

import com.entisy.gameengine.Window;
import com.entisy.gameengine.component.components.SpriteRenderer;
import com.entisy.gameengine.util.ResourceLocation;
import org.joml.Vector4f;

public class RenderBatch {
    /**
     * Vertex structure
     * might change later {@link com.entisy.gameengine.util.Vertex}
     *
     * Position             Color
     * float, float         float, float float, float
     */

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private Shader shader;

    private float[] vertices;

    private int numSprites;
    private int vaoID, vboID;
    private int maxBatchSize;
    private boolean hasRoom;

    public RenderBatch(int maxBatchSize) {
        shader = new Shader(ResourceLocation.shader("main").pointsTo());
        shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteRenderer renderer) {
        int index = numSprites;
        sprites[index] = renderer;
        numSprites++;

        loadVertexProperties(index);

        if (numSprites >= maxBatchSize)
            hasRoom = false;
    }

    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer renderer = this.sprites[index];
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = renderer.getColor();
        float xAdd = 1;
        float yAdd = 1;

        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1 -> yAdd = 0;
                case 2 -> xAdd = 0;
                case 3 -> yAdd = 1;
            }
        }

        vertices[offset] = renderer.gameObject.transform.position.x + (xAdd * renderer.gameObject.transform.scale.x);
        vertices[offset + 1] = renderer.gameObject.transform.position.y + (yAdd * renderer.gameObject.transform.scale.y);

        vertices[offset + 2] = color.x;
        vertices[offset + 3] = color.y;
        vertices[offset + 4] = color.z;
        vertices[offset + 5] = color.w;

        offset += VERTEX_SIZE;
    }

    private int[] generateIndices() {
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset; // + 0

        elements[offsetArrayIndex + 3] = offset; // + 0
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }
}
