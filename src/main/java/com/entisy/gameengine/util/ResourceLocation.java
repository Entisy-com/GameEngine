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
        return namespace;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return String.format("%s:%s", namespace, id);
    }

    public static ResourceLocation shader(String fileName) {
        return new ResourceLocation("ge", String.format("src/main/resources/shader/%s.glsl", fileName));
    }

    public static ResourceLocation texture(String x) {
        return new ResourceLocation("ge", String.format("src/main/resources/sprites/%s.png", x));
    }
}
