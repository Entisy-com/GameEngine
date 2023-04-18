package com.entisy.gameengine.util;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projectionMatrix, viewMatrix;
    public Vector3f position;

    public Camera(float posX, float posY, float posZ) {
        this.position = new Vector3f(posX, posY, posZ);
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.adjustProjection();
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();
        this.projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix() {
        var cameraFront = new Vector3f(0.0f,0.0f, -1.0f);
        var cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(this.position.x, this.position.y, 20.0f), cameraFront.add(this.position.x, this.position.y, 0.0f), cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }


}
