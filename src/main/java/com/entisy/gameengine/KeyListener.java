package com.entisy.gameengine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static KeyListener instance;

    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {
    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key < KeyListener.get().keyPressed.length) {
                KeyListener.get().keyPressed[key] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (key < KeyListener.get().keyPressed.length) {
                KeyListener.get().keyPressed[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int key) {
        if (key < KeyListener.get().keyPressed.length) {
            return KeyListener.get().keyPressed[key];
        }
        return false;
    }

}
