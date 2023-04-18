package com.entisy.gameengine;


import com.entisy.gameengine.scene.Scene;
import com.entisy.gameengine.scene.scenes.*;
import org.joml.Vector2i;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window instance = null;
    private final int width = 1920, height = 1080;
    private final String title = "GameEngine";
    private final Logger logger = LoggerFactory.getLogger(Window.class);

    private long glfwWindow;

    public float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;

    private Scene currentScene;

    private Window() {
    }

    public static Scene getCurrentScene() {
        return Window.get().currentScene;
    }

    public static Window get() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }
        return Window.instance;
    }

    public static void changeScene(int scene) {
        switch (scene) {
            case 0 -> {
                Window.get().currentScene = new LevelEditorScene();
                Window.get().currentScene.init();
                Window.get().currentScene.start();
            }
            case 1 -> {
                Window.get().currentScene = new LevelScene();
                Window.get().currentScene.init();
                Window.get().currentScene.start();
            }
            default -> {
                assert false : "Unknown scene '" + scene + "'!";
            }
        }
    }

    public void run() {
        this.logger.info(String.format("Loaded LWJGL v%s!", Version.getVersion()));

        this.init();
        this.loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW!");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (this.glfwWindow == NULL) throw new IllegalStateException("Failed to create GLFW window!");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        this.logger.info("Loaded CursorPositionCallback!");
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        this.logger.info("Loaded MouseButtonCallback!");
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        this.logger.info("Loaded ScrollCallback!");
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        this.logger.info("Loaded KeyCallback!");

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        Window.changeScene(0);
    }

    public void loop() {
        var beginTime = (float) glfwGetTime();
        float endTime;
        var dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                this.currentScene.update(dt);
            }


            glfwSwapBuffers(glfwWindow);
            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public Vector2i getSize() {
        return new Vector2i(this.width, this.height);
    }

    public String getTitle() {
        return this.title;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
