package opengl.engine.effects;

import javax.media.opengl.GL2;

public abstract class Effect {
    final protected GL2 gl;
    private boolean enabled;

    public Effect(GL2 gl) {
        this.gl = gl;
    }

    public void setup() {
    }

    public abstract void render();

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void toggle(boolean enabled) {
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }

    public void toggle() {
        toggle(!enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

}
