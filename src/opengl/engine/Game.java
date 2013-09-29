package opengl.engine;

import com.jogamp.opengl.util.texture.Texture;
import opengl.engine.effects.FlashLight;
import opengl.engine.effects.NightVision;
import opengl.engine.effects.ToonShader;
import opengl.resources.*;
import opengl.model.Model;
import opengl.util.CollectionUtils;
import opengl.util.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Game implements GLEventListener {
    private final GLU glu = new GLU();

    private final static float BLOCK_WIDTH = 3.0f;
    private final static float PLAYER_HEIGHT = 3.75f;
    private boolean useGouraud = true;
    private boolean useLighting = true;
    private boolean[] lights = new boolean[World.MAX_LIGHTS];

    private ResourceRepository<Texture> textureRepository;
    private ResourceRepository<Model> modelRepository;
    private ResourceRepository<World> worldRepository;

    private Camera camera;
    private World world;
    private WorldRenderer worldRenderer;
    private NightVision nightVision;
    private FlashLight flashLight;
    private ToonShader toonShader;
    private ModelCompiler modelCompiler;

    public Game() {
        textureRepository = new ResourceRepository<Texture>(new TextureLoader());
        modelRepository = new ResourceRepository<Model>(new ModelLoader(textureRepository));
        worldRepository = new ResourceRepository<World>(new WorldLoader(modelRepository));

        Arrays.fill(lights, true);
    }

    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        camera = new Camera();
        modelCompiler = new ModelCompiler(gl);
        worldRenderer = new WorldRenderer(gl);

        world = loadWorld("room.txt");

        GLUtils.setupContext(gl);
        GLUtils.setupLights(gl, GL2.GL_LIGHT0, world.getLights());

        nightVision = new NightVision(gl, GL2.GL_LIGHT6);
        flashLight = new FlashLight(gl, GL2.GL_LIGHT7, PLAYER_HEIGHT / 3.0f, BLOCK_WIDTH);
        toonShader = new ToonShader(gl);

        nightVision.setup();
        flashLight.setup();
        toonShader.setup();

        camera.setPosition(BLOCK_WIDTH * (world.getStart().x - BLOCK_WIDTH / 2.0f + 1), BLOCK_WIDTH * (world.getStart().y - BLOCK_WIDTH / 2.0f + 1));
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        debug("Game.reshape()");

        final GL2 gl = drawable.getGL().getGL2();

        if (height <= 0) {
            height = 1;
        }

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        glu.gluPerspective(45.0f, (float) width / (float) height, 0.1f, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        GLUtils.toggleGouraud(gl, useGouraud);
        GLUtils.toggleLighting(gl, useLighting);
        GLUtils.toggleLights(gl, GL2.GL_LIGHT0, lights);

        updateCamera();

        gl.glRotatef(camera.getVerticalHeading(), 1.0f, 0.0f, 0.0f);
        gl.glRotatef(360 - camera.getHorizontalHeading(), 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(-camera.getX(), -camera.getWalkBias() - PLAYER_HEIGHT, -camera.getZ());

        toonShader.start();
        nightVision.render();
        flashLight.render();
        worldRenderer.render(world, BLOCK_WIDTH);
        toonShader.stop();

        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        debug("Game.displayChanged()");
    }

    public void dispose(GLAutoDrawable drawable) {
        debug("Game.dispose()");
    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    //  STEROWANIE
    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    public void mouseMove(int deltaX, int deltaY) {
        turn(deltaX);
        look(deltaY);
    }

    public void stepForward(boolean step) {
        camera.stepForward(step);
    }

    public void stepBackward(boolean step) {
        camera.stepBackward(step);
    }

    public void stepLeft(boolean step) {
        camera.stepLeft(step);
    }

    public void stepRight(boolean step) {
        camera.stepRight(step);
    }

    public void turn(int turnDelta) {
        camera.turn(turnDelta);
    }

    public void look(int lookDelta) {
        camera.look(lookDelta);
    }

    public void resetLook() {
        camera.resetLook();
    }

    public void insertModel() {
        Model model = null;

        do {
            model = CollectionUtils.randomElement(modelRepository.values());
        } while (model.isBlock() || model.isFloor());

        model = new Model(model);
        model.setRotate(new Random().nextFloat() * 360f);

        world.addModel(model, positionToBlock(camera.getX()) - 1, positionToBlock(camera.getZ()) - 1);
    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    //  USTAWIENIA
    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    public void toggleShader() {
        toonShader.toggle();
    }

    public void toggleGouraud() {
        useGouraud = !useGouraud;
    }

    public void toggleLighting() {
        useLighting = !useLighting;
    }

    public void toggleLight(int i) {
        if (i >= 0 && i < lights.length) {
            lights[i] = !lights[i];
        }
    }

    public void switchLights(boolean enabled) {
        Arrays.fill(lights, enabled);
    }

    public void toggleNightVision() {
        switchLights(nightVision.isEnabled());
        flashLight.disable();
        nightVision.toggle();
    }

    public void toggleFlashLight() {
        switchLights(flashLight.isEnabled());
        nightVision.disable();
        flashLight.toggle();
    }

    private World loadWorld(String name) {
        World world = null;

        try {
            debug("LOADING WORLD");

            world = worldRepository.get(name);

            debug("COMPILING MODELS");

            for (Model model : modelRepository.values()) {
                modelCompiler.compile(model);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return world;
    }

    private void updateCamera() {
        Point2D.Float position = camera.getPosition();

        camera.turnHead();
        camera.move();

        if (world.isBlock(positionToBlock(camera.getX()) - 1, positionToBlock(camera.getZ()) - 1)) {
            camera.setPosition((float) position.getX(), (float) position.getY());
        }
    }

    private int positionToBlock(float position) {
        return (int) (position / BLOCK_WIDTH + BLOCK_WIDTH / 2.0f);
    }

    private static void debug(String message) {
        System.out.println("");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(message);
        System.out.println("--------------------------------------------------------------------------------");
    }

}
