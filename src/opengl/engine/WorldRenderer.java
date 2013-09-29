package opengl.engine;

import opengl.engine.ModelAnimator;
import opengl.engine.ModelRenderer;
import opengl.engine.World;
import opengl.model.Model;

import javax.media.opengl.GL2;
import java.util.List;

public class WorldRenderer {
    final private GL2 gl;
    final private ModelRenderer modelRenderer;
    final private ModelAnimator modelAnimator;

    public WorldRenderer(GL2 gl) {
        this.gl = gl;
        this.modelRenderer = new ModelRenderer(gl);
        this.modelAnimator = new ModelAnimator(gl, modelRenderer);
    }

    public void render(World world, float blockWidth) {
        for (int i = 0; i < world.getHeight(); i++) {
            gl.glPushMatrix();
            for (int j = 0; j < world.getWidth(); j++) {
                int lightIndex = world.getLightMap()[i][j];
                if (lightIndex != -1) {
                    gl.glLightfv(GL2.GL_LIGHT0 + lightIndex, GL2.GL_POSITION, world.getLights()[lightIndex][3], 0);
                }

                List<Model> models = world.getModels(j, i);
                if (models != null) {
                    for (Model model : models) {
                        if (model.animated) {
                            modelAnimator.animate(model);
                        } else {
                            modelRenderer.render(model);
                        }
                    }
                }
                gl.glTranslatef(blockWidth, 0.0f, 0.0f);
            }
            gl.glPopMatrix();
            gl.glTranslatef(0.0f, 0.0f, blockWidth);
        }
    }

}
