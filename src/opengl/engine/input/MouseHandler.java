package opengl.engine.input;

import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import opengl.engine.Game;

/**
 * Created by karol on 27.09.2013.
 */
public class MouseHandler extends MouseAdapter {
    private final Game game;
    private final GLWindow glWindow;

    public MouseHandler(Game game, GLWindow glWindow) {
        this.game = game;
        this.glWindow = glWindow;
    }

    private int centeredX = -1;
    private int centeredY = -1;

    @Override
    public void mouseMoved(MouseEvent me) {
        if (centeredX == -1 || centeredY == -1) {
            center();
            return;
        }

        int deltaX = me.getX() - centeredX;
        int deltaY = me.getY() - centeredY;

        game.mouseMove(deltaX, deltaY);

        center();
    }

    private void center() {
        centeredX = glWindow.getWidth() / 2;
        centeredY = glWindow.getHeight() / 2;
        glWindow.warpPointer(centeredX, centeredY);
    }

}
