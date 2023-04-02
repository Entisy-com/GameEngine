package com.entisy.gameengine.util;

public class Time {
    public static float timeStarted = System.nanoTime();
    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) / 1_000_000_000);
    }


}
