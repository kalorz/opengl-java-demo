/*
 * NewClass.java
 *
 * Utworzono February 8, 2006, 11:08 AM
 */

package opengl.model;

public class Mesh {
    private String name;
    private float[][] vertices = new float[0][0];
    private float[][] normals = new float[0][0];
    private int[][] triangles = new int[0][0];
    private int materialIndex;

    public float[][] getVertices() {
        return vertices;
    }

    public void setVertices(float[][] vertices) {
        this.vertices = vertices;
    }

    public float[][] getNormals() {
        return normals;
    }

    public void setNormals(float[][] normals) {
        this.normals = normals;
    }

    public int[][] getTriangles() {
        return triangles;
    }

    public void setTriangles(int[][] triangles) {
        this.triangles = triangles;
    }

    public int getMaterialIndex() {
        return materialIndex;
    }

    public void setMaterialIndex(int materialIndex) {
        this.materialIndex = materialIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: \"" + getName() + "\", " + getVertices().length + " vertices, " +
                getNormals().length + " normals, " + getTriangles().length + " triangles";
    }

}