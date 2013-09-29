package opengl.engine.effects;

import javax.media.opengl.GL2;
import java.util.Arrays;

public class FlashLight extends Effect {
    float[] AMBIENT = {1.0f, 1.0f, 1.0f, 1.0f};
    float[] DIFFUSE = {1.0f, 1.0f, 1.0f, 1.0f};
    float[] SPECULAR = {0.0f, 0.0f, 0.0f, 1.0f};
    float[] POSITION = {0.0f, 0.0f, 0.0f, 1.0f};
    //float[] lightDirection = { 0.0f, 0.0f, 1.0f };

    final private int light;
    private float[] position;

    public FlashLight(GL2 gl, int light, float height, float distance) {
        super(gl);

        this.light = light;
        position = Arrays.copyOf(POSITION, POSITION.length);
        position[1] = height;
        position[2] = distance;
    }

    public void setup() {
        gl.glLightfv(light, GL2.GL_AMBIENT, AMBIENT, 0);
        gl.glLightfv(light, GL2.GL_DIFFUSE, DIFFUSE, 0);
        gl.glLightfv(light, GL2.GL_SPECULAR, SPECULAR, 0);
        gl.glLightfv(light, GL2.GL_POSITION, position, 0);
        gl.glLightf(light, GL2.GL_CONSTANT_ATTENUATION, 0.8f);
        //gl.glLightfv(light, GL2.GL_SPOT_DIRECTION, lightDirection, 0);
        gl.glLightf(light, GL2.GL_SPOT_EXPONENT, 64.0f);
        gl.glLightf(light, GL2.GL_SPOT_CUTOFF, 30.0f);
    }

    public void render() {
        if (isEnabled()) {
            gl.glEnable(light);
        } else {
            gl.glDisable(light);
        }
    }

}
