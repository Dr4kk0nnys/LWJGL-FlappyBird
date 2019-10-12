package com.dr4kk0nnys.Flappy_Bird;

import com.dr4kk0nnys.Flappy_Bird.Dimensions.Dimensions;
import com.dr4kk0nnys.Flappy_Bird.Graphics.Shader;
import com.dr4kk0nnys.Flappy_Bird.Input.Input;
import com.dr4kk0nnys.Flappy_Bird.Maths.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main implements Runnable, Dimensions {

    private boolean running = false;

    private long window;

    private void start() {
        this.running = true;
        Thread game = new Thread(this, "Game");
        game.start();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // Window hints for Linux <3
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        String TITLE = "Flappy Bird";
        this.window = glfwCreateWindow(Dimensions.WIDTH, Dimensions.HEIGHT, TITLE, glfwGetPrimaryMonitor(), NULL);

        if (this.window == NULL) {
            throw new RuntimeException("Unable to create window");
        }

        glfwSetKeyCallback(this.window, new Input());

        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);
        glfwShowWindow(this.window);
        GL.createCapabilities();

        glClearColor(1f, 1f, 1f, 1f);
        glEnable(GL_DEPTH_TEST);
        System.out.println("Open GL Version: " + glGetString(GL_VERSION));

        Shader.loadAll();

        Matrix4f pr_matrix = Matrix4f.orthographic(-16f, 16f, -9f, 9f, -1f, 1f);

        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);

    }

    public void run() {
        this.init();
        while (running) {
            this.update();
            this.render();

            if (glfwWindowShouldClose(this.window)) running = false;
        }

        glfwDestroyWindow(this.window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void update() {
        glfwPollEvents();
        if (Input.keys[GLFW_KEY_SPACE]) {
            System.out.println("Yaks");
        } else if (Input.keys[GLFW_KEY_ESCAPE]) {
            this.running = false;
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_TEST);
        glfwSwapBuffers(this.window);
    }

    public static void main(String[] args) {

        new Main().start();
    }
}
