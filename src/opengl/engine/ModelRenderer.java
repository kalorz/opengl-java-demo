package opengl.engine;

import opengl.model.Model;

import javax.media.opengl.GL2;

public class ModelRenderer {
    private GL2 gl;

    public ModelRenderer(GL2 gl) {
        this.gl = gl;
    }

    public void render(Model model) {
        for (int i = 0; i < model.getMeshes().length; i++) {
            gl.glCallList(model.getDisplayList() + i);
        }
    }

}
