package opengl.engine.effects;

import opengl.engine.GLUtil;
import opengl.engine.ResourceRepository;
import opengl.engine.ShaderLoader;

import javax.media.opengl.GL2;
import java.io.IOException;

public class ToonShader extends Effect {
    private int shader;
    private ResourceRepository<String> shaderRepository;

    public ToonShader(GL2 gl) {
        super(gl);

        shaderRepository = new ResourceRepository<String>(new ShaderLoader());
    }

    public void setup() {
        try {
            int vertex = GLUtil.compileShader(gl, GL2.GL_VERTEX_SHADER, shaderRepository.get("Vertex.glsl"));
            int fragment = GLUtil.compileShader(gl, GL2.GL_FRAGMENT_SHADER, shaderRepository.get("Fragment.glsl"));

            shader = GLUtil.compileShaderProgram(gl, new int[] {vertex, fragment});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void render() {
        GLUtil.toggleShader(gl, shader, isEnabled());
    }

}
