package com.entisy.gameengine.util;

public class ResourceLocation {
    private String namespace = "ge";
    private final String id;

    public ResourceLocation(String id) {
        this.id = id;
    }

    public ResourceLocation(String namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getId() {
        return this.id;
    }

    public String getPath() {
        return String.format("%s:%s", namespace, id);
    }

    public String pointsToImage() {
        return String.format("src/main/resources/sprites/%s.png", this.id);
    }

    public String pointsToShader() {
        return String.format("src/main/resources/shader/%s.glsl", this.id);
    }

    public String pointsTo() {
        return String.format("src/main/resources/%s", this.id);
    }

    public static ResourceLocation shader(String fileName) {
        return new ResourceLocation("ge", String.format("shader/%s.glsl", fileName));
    }

    public static ResourceLocation texture(String x) {
        return new ResourceLocation("ge", String.format("src/main/resources/sprites/%s.png", x));
    }
}
