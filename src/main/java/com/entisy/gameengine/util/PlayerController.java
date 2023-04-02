package com.entisy.gameengine.util;

import com.entisy.gameengine.Camera;
import com.entisy.gameengine.KeyListener;

import java.awt.event.KeyEvent;

public class PlayerController {
    private static PlayerController instance;
    private PlayerController() {}

    public static PlayerController get() {
        if (PlayerController.instance == null)
            PlayerController.instance = new PlayerController();
        return PlayerController.instance;
    }

    public void update(float speed, Camera camera, float dt) {
        move(speed, camera, dt);
    }

    private void move(float speed, Camera camera, float dt) {
        if (KeyListener.isKeyPressed(KeyEvent.VK_A) && KeyListener.isKeyPressed(KeyEvent.VK_D) ||
                KeyListener.isKeyPressed(KeyEvent.VK_W) && KeyListener.isKeyPressed(KeyEvent.VK_S)) {
            System.out.println();
        }

        else if (KeyListener.isKeyPressed(KeyEvent.VK_W) && KeyListener.isKeyPressed(KeyEvent.VK_A)) {
            camera.position.y -= (speed / Math.sqrt(2)) * dt;
            camera.position.x += (speed / Math.sqrt(2)) * dt;
        }
        else if (KeyListener.isKeyPressed(KeyEvent.VK_W) && KeyListener.isKeyPressed(KeyEvent.VK_D)) {
            camera.position.y -= (speed / Math.sqrt(2)) * dt;
            camera.position.x -= (speed / Math.sqrt(2)) * dt;
        }
        else if (KeyListener.isKeyPressed(KeyEvent.VK_S) && KeyListener.isKeyPressed(KeyEvent.VK_A)) {
            camera.position.y += (speed / Math.sqrt(2)) * dt;
            camera.position.x += (speed / Math.sqrt(2)) * dt;
        }
        else if (KeyListener.isKeyPressed(KeyEvent.VK_S) && KeyListener.isKeyPressed(KeyEvent.VK_D)) {
            camera.position.y += (speed / Math.sqrt(2)) * dt;
            camera.position.x -= (speed / Math.sqrt(2)) * dt;
        }
        else if (KeyListener.isKeyPressed(KeyEvent.VK_W)) {
            camera.position.y -= speed * dt;
        }
        else if (KeyListener.isKeyPressed(KeyEvent.VK_S)) {
            camera.position.y += speed * dt;
        }
        else if (KeyListener.isKeyPressed(KeyEvent.VK_A)) {
            camera.position.x += speed * dt;
        }
        else if (KeyListener.isKeyPressed(KeyEvent.VK_D)) {
            camera.position.x -= speed * dt;
        }
    }
}
