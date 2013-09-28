/*
 * HelpOverlay.java
 *
 * Created on 6 luty 2006, 01:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package opengl.engine;

import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.KeyStroke;

public class HelpOverlay implements GLEventListener {
    private List<String> keyboardEntries = new ArrayList();
    private List<String> mouseEntries = new ArrayList();
    private boolean visible = true;
    private final GLUT glut = new GLUT();
    private final GLU glu = new GLU();
    private static final int CHAR_HEIGHT = 13;
    private static final int OFFSET = 13;
    private static final int INDENT = 8;
    private static final String KEYBOARD_CONTROLS = "Klawiatura";
    private static final String MOUSE_CONTROLS = "Myszka";

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void toggle() {
        visible = !visible;
    }

    public void display(GLAutoDrawable drawable) {
        if (!visible) {
            return;
        }

        final GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glViewport(0, 0, drawable.getWidth(), drawable.getHeight());

        // Store enabled state and disable lighting, texture mapping and the depth buffer
        gl.glPushAttrib(GL2.GL_ENABLE_BIT);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL2.GL_DEPTH_TEST);

        // Retrieve the current viewport and switch to orthographic mode
        int viewPort[] = new int[4];
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewPort, 0);
        glu.gluOrtho2D(0, viewPort[2], viewPort[3], 0);

        int x = OFFSET;
        int y = OFFSET + CHAR_HEIGHT;

        int maxX = OFFSET + glut.glutBitmapLength(GLUT.BITMAP_8_BY_13, KEYBOARD_CONTROLS);
        for (String entry : keyboardEntries) {
            maxX = Math.max(maxX, OFFSET + glut.glutBitmapLength(GLUT.BITMAP_8_BY_13, entry));
        }

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(0f, 0f, 0f, 0.5f);
        gl.glRecti(OFFSET - 4, OFFSET - 6, maxX + INDENT + 4, 6 + OFFSET + OFFSET + CHAR_HEIGHT * Math.max(keyboardEntries.size(), mouseEntries.size()));
        gl.glDisable(GL2.GL_BLEND);

        // Render the text
        gl.glColor3f(1f, 1f, 1f);

        if (keyboardEntries.size() > 0) {
            gl.glRasterPos2i(x, y);
            glut.glutBitmapString(GLUT.BITMAP_8_BY_13, KEYBOARD_CONTROLS);

            y += OFFSET;
            x += INDENT;
            for (String entry : keyboardEntries) {
                gl.glRasterPos2f(x, y);
                glut.glutBitmapString(GLUT.BITMAP_8_BY_13, entry);
                y += OFFSET;
            }
        }

        if (mouseEntries.size() > 0) {
            x = maxX + OFFSET;
            y = OFFSET + CHAR_HEIGHT;

            gl.glRasterPos2i(x, y);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, MOUSE_CONTROLS);

            y += OFFSET;
            x += INDENT;
            for (String entry : mouseEntries) {
                gl.glRasterPos2f(x, y);
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, entry);
                y += OFFSET;
            }
        }

        // Restore enabled state
        gl.glPopAttrib();

        // Restore old matrices
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPopMatrix();
    }

    public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
    }

    public void init(GLAutoDrawable glDrawable) {
    }

    public void reshape(GLAutoDrawable glDrawable, int i, int i1, int i2, int i3) {
    }

    public void registerKeyStroke(String keyStroke, String description) {
        keyboardEntries.add(keyStroke + ": " + description);
    }

    public void registerKeyStroke(KeyStroke keyStroke, String description) {
        String modifiersText = KeyEvent.getKeyModifiersText(keyStroke.getModifiers());
        String keyText = KeyEvent.getKeyText(keyStroke.getKeyCode());
        registerKeyStroke(
                (modifiersText.length() > 0 ? modifiersText + " " : "") + keyText,
                description
        );
    }

    public void registerMouseEvent(int id, int modifiers, String description) {
        String mouseText = null;
        switch (id) {
            case MouseEvent.MOUSE_CLICKED:
                mouseText = "Clicked " + MouseEvent.getModifiersExText(modifiers);
                break;
            case MouseEvent.MOUSE_DRAGGED:
                mouseText = "Dragged " + MouseEvent.getModifiersExText(modifiers);
                break;
            case MouseEvent.MOUSE_ENTERED:
                mouseText = "Mouse enters";
                break;
            case MouseEvent.MOUSE_EXITED:
                mouseText = "Mouse exits";
                break;
            case MouseEvent.MOUSE_MOVED:
                mouseText = "Mouse moves";
                break;
            case MouseEvent.MOUSE_PRESSED:
                mouseText = "Pressed " + MouseEvent.getModifiersExText(modifiers);
                break;
            case MouseEvent.MOUSE_RELEASED:
                mouseText = "Released " + MouseEvent.getModifiersExText(modifiers);
                break;
        }
        mouseEntries.add(
                mouseText + ": " + description
        );
    }

    public void dispose(GLAutoDrawable drawable) {
        System.out.println("");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("HelpOverlay.dispose()");
        System.out.println("--------------------------------------------------------------------------------");
    }
    
}
