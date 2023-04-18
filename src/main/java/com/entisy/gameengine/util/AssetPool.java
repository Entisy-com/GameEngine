package com.entisy.gameengine.util;

import com.entisy.gameengine.render.Shader;
import com.entisy.gameengine.render.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private final static Map<String, Shader> shaders = new HashMap<>();
    private final static Map<String, Texture> textures = new HashMap<>();

    public static Shader getShader(ResourceLocation loc) {
        var file = new File(loc.pointsTo());
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            var shader = new Shader(loc.pointsTo());
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(ResourceLocation loc) {
        var file = new File(loc.pointsToImage());
        if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            var texture = new Texture(loc.pointsToImage());
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }
}
