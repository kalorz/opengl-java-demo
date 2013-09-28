/*
 * QuitAdapter.java
 *
 * Created on 6 luty 2006, 02:05
 */
package opengl.engine.input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;

public class QuitHandler extends WindowAdapter implements WindowListener, KeyListener {

    boolean shouldQuit = false;
    boolean enabled = true;

    public void enable(boolean v) {
        enabled = v;
    }

    public void clear() {
        shouldQuit = false;
    }

    public boolean shouldQuit() {
        return shouldQuit;
    }

    @Override
    public void windowDestroyNotify(WindowEvent e) {
        if (enabled) {
            shouldQuit = true;
        }
    }

    public void keyTyped(KeyEvent e) {
        if (enabled) {
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                shouldQuit = true;
            }
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}