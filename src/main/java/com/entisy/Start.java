package com.entisy;

import com.entisy.gameengine.Window;
import jdk.jfr.Description;

public class Start {
    public static void main(String[] args) {
        Window window = Window.get();
        window.run();
    }
}
