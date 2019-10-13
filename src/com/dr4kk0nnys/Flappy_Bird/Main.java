package com.dr4kk0nnys.Flappy_Bird;

import com.dr4kk0nnys.Flappy_Bird.Dimensions.Dimensions;
import com.dr4kk0nnys.Flappy_Bird.Graphics.Shader;
import com.dr4kk0nnys.Flappy_Bird.Input.Input;
import com.dr4kk0nnys.Flappy_Bird.Level.Level;
import com.dr4kk0nnys.Flappy_Bird.Maths.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main implements Runnable, Dimensions {

    private final String TITLE = "Flappy Bird";

    private boolean running = false;

    private long window;

    private Level level;

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

        this.window = glfwCreateWindow(Dimensions.WIDTH, Dimensions.HEIGHT, this.TITLE, glfwGetPrimaryMonitor(), NULL);

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
        glActiveTexture(GL_TEXTURE1);
        System.out.println("Open GL Version: " + glGetString(GL_VERSION));

        Shader.loadAll();

        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);

        level = new Level();
    }

    public void run() {
        this.init();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1.0) {
                update();
                updates++;
                delta--;
            }
            this.render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                glfwSetWindowTitle(this.window, this.TITLE + " | FPS: " + frames);
                updates = 0;
                frames = 0;
            }

            if (glfwWindowShouldClose(this.window)) running = false;
        }

        glfwDestroyWindow(this.window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void update() {
        glfwPollEvents();
        level.update();
        if (Input.keys[GLFW_KEY_SPACE]) {
            System.out.println("Yaks");
        } else if (Input.keys[GLFW_KEY_ESCAPE]) {
            this.running = false;
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println(error);
        }
        glfwSwapBuffers(this.window);
    }

    public static void main(String[] args) {

        new Main().start();
    }
}
