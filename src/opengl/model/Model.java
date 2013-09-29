package opengl.model;

import java.util.Arrays;

public class Model {

    private Material[] materials;
    private Mesh[] meshes;
    private boolean block;
    private boolean floor;
    private int displayList;

    private float rotate;

    public boolean animated = false;
    public float[] animationDelta;
    public float[] animation;

    public Model() {
    }

    public Model(Model other) {
        this.materials = other.materials;
        this.meshes = other.meshes;
        this.block = other.block;
        this.floor = other.floor;
        this.displayList = other.displayList;

        this.rotate = other.rotate;

        this.animated = other.animated;
        this.animationDelta = other.animationDelta;

        if (other.animation != null) {
            this.animation = Arrays.copyOf(other.animation, other.animation.length);
        }
    }

    public Material[] getMaterials() {
        return materials;
    }

    public void setMaterials(Material[] materials) {
        this.materials = materials;
    }

    public Mesh[] getMeshes() {
        return meshes;
    }

    public void setMeshes(Mesh[] meshes) {
        this.meshes = meshes;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean isFloor() {
        return floor;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public void setFloor(boolean floor) {
        this.floor = floor;
    }

    public int getDisplayList() {
        return displayList;
    }

    public void setDisplayList(int displayList) {
        this.displayList = displayList;
    }

}
