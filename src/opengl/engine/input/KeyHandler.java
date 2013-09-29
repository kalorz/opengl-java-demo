package opengl.engine.input;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import opengl.engine.*;

import javax.swing.*;

public class KeyHandler extends KeyAdapter {
    private final Game game;
    private final HelpOverlay helpOverlay;

    public KeyHandler(Game game, HelpOverlay helpOverlay) {
        this.game = game;
        this.helpOverlay = helpOverlay;

        helpOverlay.registerKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Latarka");
        helpOverlay.registerKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "Noktowizor");
        helpOverlay.registerKeyStroke("1-6", "Wlacz/wylacz swiatlo");
        helpOverlay.registerKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), "Wlacz wszystkie swiatla");
        helpOverlay.registerKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0), "Wylacz wszystkie swiatla");
        helpOverlay.registerKeyStroke("[", "Cieniowanie plaskie / Gourauda");
        helpOverlay.registerKeyStroke("]", "Wlaczenie / wylaczenie obslugi swiatla");
        helpOverlay.registerKeyStroke("\\", "Wlaczenie / wylaczenie shaderow");
        helpOverlay.registerKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "Pomoc");
    }

    public void keyPressed(KeyEvent keyEvent) {
        processKeyEvent(keyEvent, true);
    }

    public void keyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            // Ustawienia
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
                game.toggleLight(keyEvent.getKeyCode() - '0' - 1);
                break;
            case KeyEvent.VK_7:
                game.switchLights(true);
                break;
            case KeyEvent.VK_8:
                game.switchLights(false);
                break;
            case KeyEvent.VK_BACK_SLASH:
                game.toggleShader();
                break;
            case KeyEvent.VK_OPEN_BRACKET:
                game.toggleGouraud();
                break;
            case KeyEvent.VK_CLOSE_BRACKET:
                game.toggleLighting();
                break;
            case KeyEvent.VK_R:
                game.toggleNightVision();
                break;
            case KeyEvent.VK_F:
                game.toggleFlashLight();
                break;
            case KeyEvent.VK_END:
                game.resetLook();
                break;
            case KeyEvent.VK_H:
                helpOverlay.toggle();
                break;
            default:
                processKeyEvent(keyEvent, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                game.stepForward(pressed);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                game.stepBackward(pressed);
                break;
            case KeyEvent.VK_A:
                game.stepLeft(pressed);
                break;
            case KeyEvent.VK_D:
                game.stepRight(pressed);
                break;
            case KeyEvent.VK_LEFT:
                game.turn(-2);
                break;
            case KeyEvent.VK_RIGHT:
                game.turn(2);
                break;
            case KeyEvent.VK_PAGE_UP:
                game.look(2);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                game.look(-2);
                break;
        }
    }
}
