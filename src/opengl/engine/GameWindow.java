/*
 * Launcher.java
 *
 * Created on 4 luty 2006, 12:59
 */
package opengl.engine;

import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import opengl.engine.input.KeyHandler;
import opengl.engine.input.MouseHandler;
import opengl.engine.input.QuitHandler;

import javax.media.opengl.FPSCounter;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

/**
 * @author Administrator
 */
public class GameWindow {

    // Tytul okienka
    private final static String WINDOW_TITLE = "Ryj kota";

    // Glowna funkcja programu
    public static void main(String[] args) throws InterruptedException {
        final Game game = new Game();
        final HelpOverlay helpOverlay = new HelpOverlay();

        final GLWindow glWindow = GLWindow.create(new GLCapabilities(GLProfile.getDefault()));
        final Animator animator = new Animator(glWindow);
        final QuitHandler quitHandler = new QuitHandler();
        final MouseListener mouseHandler = new MouseHandler(game, glWindow);
        final KeyListener keyHandler = new KeyHandler(game, helpOverlay);

        glWindow.addGLEventListener(game);
        glWindow.addGLEventListener(helpOverlay);
        glWindow.addMouseListener(mouseHandler);
        glWindow.addKeyListener(keyHandler);
        glWindow.addKeyListener(quitHandler);
        glWindow.addWindowListener(quitHandler);

        animator.setUpdateFPSFrames(FPSCounter.DEFAULT_FRAMES_PER_INTERVAL, null);
        animator.start();

        glWindow.setSize(800, 600);
        glWindow.setTitle(WINDOW_TITLE);
        // glWindow.setAlwaysOnTop(true);
        glWindow.setPointerVisible(false);
        glWindow.setVisible(true);
        glWindow.requestFocus();
        glWindow.confinePointer(true);

        while (!quitHandler.shouldQuit()) {
            Thread.sleep(50);
        }

        new Thread() {
            @Override
            public void run() {
                animator.stop(); // stop the animator loop
                glWindow.destroy();

                System.exit(0);
            }
        }.start();
    }
}
