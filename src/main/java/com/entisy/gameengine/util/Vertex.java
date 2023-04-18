package com.entisy.gameengine.util;

import jdk.jfr.Description;
import org.joml.Vector2f;
import org.joml.Vector4f;

@Deprecated
@Description("Might use later for code optimization and readability")
public class Vertex {
    public Vector2f position;
    public Vector4f color;
    public Vector2f texCoords;
    public float texID;

    public Vertex(float x, float y, float r, float g, float b, float a, float posX, float posY, float texID) {
        this.position = new Vector2f(x, y);
        this.color = new Vector4f(r, g, b, a);
        this.texCoords = new Vector2f(posX, posY);
        this.texID = texID;
    }
}
