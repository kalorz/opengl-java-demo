package opengl.engine;

import java.awt.geom.Point2D;

public class Camera {
    private final double PI_180 = Math.PI / 180.0d;
    private static float sinTable[], cosTable[], sinTable2[];

    private float xpos;
    private float zpos;
    private int horizontalHeading;
    private int verticalHeading;
    private int walkBiasAngle;
    private float walkBias;

    private boolean stepForward;
    private boolean stepBackward;
    private boolean stepLeft;
    private boolean stepRight;
    private int turnDelta;
    private int lookDelta;

    public Camera() {
        // Budowanie tablic z przeliczonymi wartosciami sin/cos dla odpowiednich katow.
        // Powinno dzialac szybciej niz przeliczanie tych wartosci "na zywo"
        // przy pomocy Math.sin(angle).
        // Byc moze tego typu optymalizacja jest juz wbudowana w Math.sin.
        sinTable  = new float[360];
        cosTable  = new float[360];
        sinTable2 = new float[360];

        for (int index = 0; index < 360; index++) {
            sinTable[index]  = (float) Math.sin(index * PI_180) * 0.15f;
            cosTable[index]  = (float) Math.cos(index * PI_180) * 0.15f;
            sinTable2[index] = (float) Math.sin(index * PI_180) / 15.0f;
        }
    }

    public Point2D.Float getPosition() {
        return new Point2D.Float(xpos, zpos);
    }

    public void setPosition(float x, float z) {
        xpos = x;
        zpos = z;
    }

    public float getX() {
        return xpos;
    }

    public float getZ() {
        return zpos;
    }

    public int getVerticalHeading() {
        return verticalHeading;
    }

    public int getHorizontalHeading() {
        return horizontalHeading;
    }

    public float getWalkBias() {
        return walkBias;
    }

    public void stepForward(boolean step) {
        stepForward = step;
    }

    public void stepBackward(boolean step) {
        stepBackward = step;
    }

    public void stepLeft(boolean step) {
        stepLeft = step;
    }

    public void stepRight(boolean step) {
        stepRight = step;
    }

    public void turn(int turnDelta) {
        this.turnDelta = turnDelta;
    }

    public void look(int lookDelta) {
        this.lookDelta = lookDelta;
    }

    public void resetLook() {
        verticalHeading = 0;
    }

    public void turnHead() {
        if (turnDelta != 0) {
            horizontalHeading -= turnDelta;

            if (horizontalHeading < 0) {
                horizontalHeading += 360;
            } else if (horizontalHeading > 359) {
                horizontalHeading -= 360;
            }
        }

        if (lookDelta > 0 && verticalHeading < 60) {
            verticalHeading += lookDelta;
        }

        if (lookDelta < 0 && verticalHeading > -60) {
            verticalHeading += lookDelta;
        }

        lookDelta = turnDelta = 0;
    }

    public void move() {
        float newXpos = xpos;
        float newZpos = zpos;

        if (stepForward) {
            newXpos -= sinTable[horizontalHeading];
            newZpos -= cosTable[horizontalHeading];
        }

        if (stepBackward) {
            newXpos += sinTable[horizontalHeading];
            newZpos += cosTable[horizontalHeading];
        }

        if (stepLeft) {
            int strafeHeading = horizontalHeading >= 90 ? horizontalHeading - 90 : horizontalHeading + 270;

            newXpos += sinTable[strafeHeading];
            newZpos += cosTable[strafeHeading];
        }

        if (stepRight) {
            int strafeHeading = horizontalHeading >= 90 ? horizontalHeading - 90 : horizontalHeading + 270;

            newXpos -= sinTable[strafeHeading];
            newZpos -= cosTable[strafeHeading];
        }

        if (stepForward || stepBackward || stepLeft || stepRight) {
            xpos = newXpos;
            zpos = newZpos;

            walkBiasAngle += 15;

            if (walkBiasAngle > 359) {
                walkBiasAngle -= 360;
            }

            walkBias = sinTable2[walkBiasAngle];
        }
    }

}
