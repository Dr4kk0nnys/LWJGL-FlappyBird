package com.dr4kk0nnys.Flappy_Bird.Graphics;

import com.dr4kk0nnys.Flappy_Bird.Maths.Matrix4f;
import com.dr4kk0nnys.Flappy_Bird.Maths.Vector3f;
import com.dr4kk0nnys.Flappy_Bird.Utils.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    public static Shader BG, BIRD, PIPE;

    private boolean enabled = false;

    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
        BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
        PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
    }

    public int getUniform(String name) {
        if(locationCache.containsKey(name)) return locationCache.get(name);

        int result = glGetUniformLocation(ID, name);
        if(result == -1) System.err.println("Couldn't find uniform variable '" + name + "'!");
        else locationCache.put(name, result);

        return result;
    }

    public void setUniform1i(String name, int value) {
        if(!this.enabled) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if(!this.enabled) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        if(!this.enabled) enable();
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector) {
        if(!this.enabled) enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if(!this.enabled) enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        this.enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        this.enabled = false;
    }

}
