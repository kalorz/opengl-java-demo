package opengl.engine.effects;

import javax.media.opengl.GL2;

public class NightVision extends Effect {
    // To swiatlo jest zawsze z poziomu "oczu" - nie trzeba przesuwac
    // jego pozycji o playerHeight
    float[] AMBIENT = {0.05f, 1.0f, 0.05f, 1.0f};
    float[] DIFFUSE = {0.05f, 1.0f, 0.05f, 1.0f};
    float[] SPECULAR = {0.0f, 0.0f, 0.0f, 1.0f};
    float[] POSITION = {0.0f, 0.0f, 0.0f, 1.0f};
    //float[] lightDirection = { 0.0f, 0.0f, 5.0f };

    // Efekt mgly zasymuluje dzialanie noktowizora
    float[] fogColor = {0.05f, 1.0f, 0.05f, 1.0f};
    
    private final int light;

    public NightVision(GL2 gl, int light) {
        super(gl);

        this.light = light;
    }
    
    public void setup() {
        gl.glFogfv(GL2.GL_FOG_COLOR, fogColor, 0);
        gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
        gl.glFogf(GL2.GL_FOG_START, -2.5f);
        gl.glFogf(GL2.GL_FOG_END, 20.0f);
        gl.glFogf(GL2.GL_FOG_DENSITY, 0.1f);

        gl.glLightfv(light, GL2.GL_AMBIENT, AMBIENT, 0);
        gl.glLightfv(light, GL2.GL_DIFFUSE, DIFFUSE, 0);
        gl.glLightfv(light, GL2.GL_SPECULAR, SPECULAR, 0);
        gl.glLightfv(light, GL2.GL_POSITION, POSITION, 0);
    }

    public void render() {
        if (isEnabled()) {
            gl.glEnable(light);
            gl.glEnable(GL2.GL_FOG);
        } else {
            gl.glDisable(light);
            gl.glDisable(GL2.GL_FOG);
        }
    }

}
