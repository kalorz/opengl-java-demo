package opengl.engine;

import opengl.model.Model;

import javax.media.opengl.GL2;

public class ModelAnimator {
    private GL2 gl;
    private ModelRenderer modelRenderer;

    public ModelAnimator(GL2 gl, ModelRenderer modelRenderer) {
        this.gl = gl;
        this.modelRenderer = modelRenderer;
    }

    public void animate(Model model) {
        model.animation[0] += model.animationDelta[0];
        model.animation[1] += model.animationDelta[1];
        model.animation[2] += model.animationDelta[2];
        gl.glPushMatrix();
        gl.glRotatef(model.animation[0], 1, 0, 0);
        gl.glRotatef(model.animation[1], 0, 1, 0);
        gl.glRotatef(model.animation[2], 0, 0, 1);
        modelRenderer.render(model);
        gl.glPopMatrix();
    }

}
