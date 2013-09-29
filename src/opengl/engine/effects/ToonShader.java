package opengl.engine.effects;

import opengl.util.GLUtils;
import opengl.resources.ResourceRepository;
import opengl.resources.ShaderLoader;

import javax.media.opengl.GL2;
import java.io.IOException;

public class ToonShader extends Effect {
    private int shaderProgram;
    private ResourceRepository<String> shaderRepository;

    public ToonShader(GL2 gl) {
        super(gl);

        shaderRepository = new ResourceRepository<String>(new ShaderLoader());
    }

    public void setup() {
        try {
            int vertex = GLUtils.compileShader(gl, GL2.GL_VERTEX_SHADER, shaderRepository.get("Vertex.glsl"));
            int fragment = GLUtils.compileShader(gl, GL2.GL_FRAGMENT_SHADER, shaderRepository.get("Fragment.glsl"));

            shaderProgram = GLUtils.compileShaderProgram(gl, new int[]{vertex, fragment});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void render() {
    }

    public void start() {
        GLUtils.toggleShader(gl, shaderProgram, isEnabled());
    }

    public void stop() {
        GLUtils.toggleShader(gl, shaderProgram, false);
    }

}
