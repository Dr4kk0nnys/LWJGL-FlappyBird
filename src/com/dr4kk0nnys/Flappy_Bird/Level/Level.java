package com.dr4kk0nnys.Flappy_Bird.Level;

import com.dr4kk0nnys.Flappy_Bird.Graphics.Shader;
import com.dr4kk0nnys.Flappy_Bird.Graphics.Texture;
import com.dr4kk0nnys.Flappy_Bird.Graphics.VertexArray;
import com.dr4kk0nnys.Flappy_Bird.Maths.Matrix4f;
import com.dr4kk0nnys.Flappy_Bird.Maths.Vector3f;

import java.util.Random;

public class Level {

    private VertexArray background;
    private Texture bgTexture;

    private int xScroll = 0, map = 0;

    private Bird bird;

    private Pipe[] pipes = new Pipe[5 * 2];

    private int index = 0;

    private Random random = new Random();

    public Level() {
        float[] vertices = new float[] {
            -10f, -10f * 9f / 16f, 0f,
            -10f, 10f * 9f / 16f, 0f,
            0f, 10f * 9f / 16f, 0f,
            0f, -10f * 9f / 16f, 0f
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/bg.jpeg");

        bird = new Bird();

        createPipes();
    }

    private void createPipes() {
        Pipe.create();
        for(int i = 0; i < 5 * 2; i += 2) {
            pipes[i] = new Pipe(index * 3f, random.nextFloat() * 4f);
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 11f);
            index += 2;
        }
    }

    private void updatePipes() {
//        pipes[]
    }

    public void update() {
        xScroll--;            /* Map movement */
        if(-xScroll % 335 == 0) map++;

        bird.update();
    }

    private void renderPipes() {
        Shader.PIPE.enable();
        Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.03f, 0f, 0f)));
        Pipe.getTexture().bind();
        Pipe.getMesh().bind();

        for(int i = 0; i < 5 * 2; i++) {
            Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
            Pipe.getMesh().draw();
        }

        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
    }

    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        background.bind();
        for(int i = map; i < map + 3; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0f, 0f)));
            background.draw();
        }
//        background.render();
        Shader.BG.disable();
        bgTexture.unbind();

        renderPipes();
        bird.render();
    }

}
