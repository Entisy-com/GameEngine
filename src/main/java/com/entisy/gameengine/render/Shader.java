package com.entisy.gameengine.render;

import com.entisy.gameengine.Window;
import jdk.jfr.Description;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Might change the way we split the file into two shaders using regex
 */
public class Shader {

    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filePath) {
        this.filePath = filePath;

        try {
            var source = new String(Files.readAllBytes(Paths.get(this.filePath)));
            var splitString = source.split("(#type)( )+([a-zA-Z]+)");

            var index = source.indexOf("#type") + 6;
            var eol = source.indexOf("\r\n", index);
            var firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            var secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                this.vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                this.fragmentSource = splitString[1];
            } else {
                throw new IOException("Error: Invalid shader type specified: '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                this.vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                this.fragmentSource = splitString[2];
            } else {
                throw new IOException("Error: Invalid shader type specified: '" + secondPattern + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: " + this.filePath;
        }

    }

    public void compile() {

        int vertexID, fragmentID;
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        var success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            var len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            Window.get().getLogger().info("'" + this.filePath + "'\n\tVertex shader compilation failed!");
            Window.get().getLogger().error("'" + glGetShaderInfoLog(vertexID, len) + "'");
            assert false : "";
        }
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            var len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            Window.get().getLogger().info("'" + this.filePath + "'\n\tFragment shader compilation failed!");
            Window.get().getLogger().error("'" + glGetShaderInfoLog(fragmentID, len) + "'");
            assert false : "";
        }
        this.shaderProgramID = glCreateProgram();
        glAttachShader(this.shaderProgramID, vertexID);
        glAttachShader(this.shaderProgramID, fragmentID);
        glLinkProgram(this.shaderProgramID);

        success = glGetProgrami(this.shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            var len = glGetProgrami(this.shaderProgramID, GL_INFO_LOG_LENGTH);
            Window.get().getLogger().info(String.format("'%s'\n\tShader link failed!", this.filePath));
            Window.get().getLogger().error(String.format("'%s'", glGetProgramInfoLog(this.shaderProgramID, len)));
            assert false : "";
        }

    }

    public void use() {
        if (!this.beingUsed) {
            glUseProgram(this.shaderProgramID);
            this.beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        this.beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        var matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        var matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array) {
        var varLocation = glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        glUniform1iv(varLocation, array);
    }
}
