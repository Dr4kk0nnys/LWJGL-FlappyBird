package com.dr4kk0nnys.Flappy_Bird.Input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scanCode, int action, int mods) {
        keys[key] = action != GLFW.GLFW_RELEASE;
    }
}
