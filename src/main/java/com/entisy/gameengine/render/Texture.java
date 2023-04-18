package com.entisy.gameengine.render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int texID;

    public Texture(String filepath) {
        this.filepath = filepath;

        this.texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        var width = BufferUtils.createIntBuffer(1);
        var height = BufferUtils.createIntBuffer(1);
        var channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        var image = stbi_load(this.filepath, width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(), height.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : String.format("Error: (Texture) Unknown number of channels '%s'", channels.get(0));
            }
            glGenerateMipmap(GL_TEXTURE_2D);
        } else {
            assert false : "Error: Could not load texture file: " + filepath;
        }

        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
