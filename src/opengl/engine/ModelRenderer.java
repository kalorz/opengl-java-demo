package opengl.engine;

import opengl.model.Model;

import javax.media.opengl.GL2;

public class ModelRenderer {
    private GL2 gl;

    public ModelRenderer(GL2 gl) {
        this.gl = gl;
    }

    public void render(Model model) {
        if (model.getRotate() != 0) {
            gl.glPushMatrix();
            gl.glRotatef(360f - model.getRotate(), 0.0f, 1.0f, 0.0f);
        }

        for (int i = 0; i < model.getMeshes().length; i++) {
            gl.glCallList(model.getDisplayList() + i);
        }

        if (model.getRotate() != 0) {
            gl.glPopMatrix();
        }
    }

}
