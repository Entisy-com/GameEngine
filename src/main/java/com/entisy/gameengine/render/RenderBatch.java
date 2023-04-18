package com.entisy.gameengine.render;

import static org.lwjgl.opengl.GL30.*;

import com.entisy.gameengine.Window;
import com.entisy.gameengine.component.components.SpriteRenderer;
import com.entisy.gameengine.util.AssetPool;
import com.entisy.gameengine.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RenderBatch {
    /**
     * Vertex structure
     * might change later {@link com.entisy.gameengine.util.Vertex}
     *
     * Position             Color                           TexCoords           texID
     * float, float         float, float float, float       float, float        float
     */

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXT_COORDS_SIZE = 2;
    private final int TEXT_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEXT_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private List<Texture> textures;
    private SpriteRenderer[] sprites;
    private Shader shader;

    private float[] vertices;

    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private int numSprites;
    private int vaoID, vboID;
    private int maxBatchSize;
    private boolean hasRoom;

    public RenderBatch(int maxBatchSize) {
        this.shader = AssetPool.getShader(ResourceLocation.shader("main"));
        this.shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        this.vertices = new float[this.maxBatchSize * 4 * this.VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void start() {
        this.vaoID = glGenVertexArrays();
        glBindVertexArray(this.vaoID);

        this.vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
        glBufferData(GL_ARRAY_BUFFER, this.vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        var eboID = glGenBuffers();
        var indices = this.generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, this.POS_SIZE, GL_FLOAT, false, this.VERTEX_SIZE_BYTES, this.POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, this.COLOR_SIZE, GL_FLOAT, false, this.VERTEX_SIZE_BYTES, this.COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, this.TEXT_COORDS_SIZE, GL_FLOAT, false, this.VERTEX_SIZE_BYTES, this.TEXT_COORDS_SIZE);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, this.TEXT_ID_SIZE, GL_FLOAT, false, this.VERTEX_SIZE_BYTES, this.TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRenderer renderer) {
        var index = this.numSprites;
        this.sprites[index] = renderer;
        this.numSprites++;

        if (renderer.getTexture() != null)
            if (!textures.contains(renderer.getTexture()))
                textures.add(renderer.getTexture());

        this.loadVertexProperties(index);

        if (this.numSprites >= this.maxBatchSize)
            this.hasRoom = false;
    }

    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        this.shader.use();
        this.shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        this.shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(this.vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i = 0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }

        this.shader.detach();
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    private void loadVertexProperties(int index) {
        var renderer = this.sprites[index];
        var offset = index * 4 * this.VERTEX_SIZE;
        var color = renderer.getColor();
        var texCoords = renderer.getTexCoords();
        var texID = 0;

        if (renderer.getTexture() != null)
            for (int i = 0; i < textures.size(); i++)
                if (textures.get(i) == renderer.getTexture()) {
                    texID = i + 1;
                    break;
                }

        var xAdd = 1.0f;
        var yAdd = 1.0f;

        for (var i = 0; i < 4; i++) {
            switch (i) {
                case 1 -> yAdd = 0.0f;
                case 2 -> xAdd = 0.0f;
                case 3 -> yAdd = 1.0f;
            }

            this.vertices[offset] = renderer.gameObject.transform.position.x + (xAdd * renderer.gameObject.transform.scale.x);
            this.vertices[offset + 1] = renderer.gameObject.transform.position.y + (yAdd * renderer.gameObject.transform.scale.y);

            this.vertices[offset + 2] = color.x;
            this.vertices[offset + 3] = color.y;
            this.vertices[offset + 4] = color.z;
            this.vertices[offset + 5] = color.w;

            this.vertices[offset + 6] = texCoords[i].x;
            this.vertices[offset + 7] = texCoords[i].y;

            this.vertices[offset + 8] = texID;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        var elements = new int[6 * this.maxBatchSize];
        for (var i = 0; i < this.maxBatchSize; i++) {
            this.loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        var offsetArrayIndex = 6 * index;
        var offset = 4 * index;

        elements[offsetArrayIndex    ] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset; // + 0

        elements[offsetArrayIndex + 3] = offset; // + 0
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }
}
