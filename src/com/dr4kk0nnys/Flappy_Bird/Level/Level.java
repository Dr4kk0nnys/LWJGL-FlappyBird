package com.dr4kk0nnys.Flappy_Bird.Level;

import com.dr4kk0nnys.Flappy_Bird.Graphics.Shader;
import com.dr4kk0nnys.Flappy_Bird.Graphics.Texture;
import com.dr4kk0nnys.Flappy_Bird.Graphics.VertexArray;
import com.dr4kk0nnys.Flappy_Bird.Maths.Matrix4f;
import com.dr4kk0nnys.Flappy_Bird.Maths.Vector3f;

public class Level {

    private VertexArray background;
    private Texture bgTexture;

    private int xScroll = 0, map = 0;

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
    }

    public void update() {
        xScroll--;
        if(-xScroll % 335 == 0) map++;
    }

    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        background.bind();
        for(int i = map; i < map + 3; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0f, 0f)));
            background.draw();
        }
        background.render();
        Shader.BG.disable();
        bgTexture.unbind();
    }

}
