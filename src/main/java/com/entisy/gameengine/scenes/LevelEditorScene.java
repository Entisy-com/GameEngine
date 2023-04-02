package com.entisy.gameengine.scenes;

import com.entisy.gameengine.Camera;
import com.entisy.gameengine.Scene;
import com.entisy.gameengine.renderer.Shader;
import com.entisy.gameengine.renderer.Texture;
import com.entisy.gameengine.util.PlayerController;
import com.entisy.gameengine.util.ResourceLocation;
import com.entisy.gameengine.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private boolean changeingScene = false;

    private float timeToChangeScene = 2.0f;

    private Shader defaultShader;
    private Texture texture;

    private float[] vertexArray = {
            // positions         // colors
            100.0f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0, 1,// bottom right 0
            -0.5f, 100.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1, 0,// top left 1
            100.0f, 100.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0, 0,// top right 2
            -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1, 1,// bottom left 3
    };

    private int vaoID, vboID, eboID;

    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3 // Bottom left triangle
    };

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader(ResourceLocation.shader("main").getId());
        defaultShader.compile();
        texture = new Texture(ResourceLocation.texture("player").getId());

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);



    }

    @Override
    public void update(float dt) {
        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMPLER",0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        defaultShader.detach();
        texture.unbind();
        // move
        PlayerController.get().update(150, this.camera, dt);
    }


}
