/*
 * Material.java
 *
 * Created on February 8, 2006, 8:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package opengl.model;

import com.jogamp.opengl.util.texture.Texture;

import java.util.Arrays;

/**
 *
 * @author mistrz
 */
public final class Material {
    private static int materialNumber;

    // Domyslne wartosci (ambient, diffuse, specular, emissive - wziete z domyslnych wartosci OpenGL)
    private final static String  DEFAULT_NAME         = "Material";
    private final static float[] DEFAULT_AMBIENT      = { 0.2f, 0.2f, 0.2f, 1.0f };
    private final static float[] DEFAULT_DIFFUSE      = { 0.8f, 0.8f, 0.8f, 1.0f };
    private final static float[] DEFAULT_SPECULAR     = { 0.0f, 0.0f, 0.0f, 1.0f };
    private final static float[] DEFAULT_EMISSIVE     = { 0.0f, 0.0f, 0.0f, 1.0f };
    private final static float   DEFAULT_SHININESS    = 0.0f;
    private final static float   DEFAULT_TRANSPARENCY = 1.0f;
    private final static Texture DEFAULT_TEXTURE      = null;
    private final static String  DEFAULT_ALPHA_MAP    = "";
    
    // Wlasciwosci materialu
    private String  name;
    private float[] ambient;      //
    private float[] diffuse;      //
    private float[] specular;     //
    private float[] emissive;     //
    private float   shininess;    // Polysk           [ 0 .. 128 ]
    private float   transparency; // Przezroczystosc  [ 0.0f .. 1.0f ]
    private Texture texture;      // Tekstura
    private String  alphaMap;     // Nazwa mapy przezroczystosci
    
    // Konstruktor
    public Material() {
        // Ustawienie domyslnych wartosci dla nowo utworzonego materialu
        setName(DEFAULT_NAME + materialNumber);
        setAmbient(DEFAULT_AMBIENT);
        setDiffuse(DEFAULT_DIFFUSE);
        setSpecular(DEFAULT_SPECULAR);
        setEmissive(DEFAULT_EMISSIVE);
        setShininess(DEFAULT_SHININESS);
        setTransparency(DEFAULT_TRANSPARENCY);
        setTexture(DEFAULT_TEXTURE);
        setAlphaMap(DEFAULT_ALPHA_MAP);
        
        materialNumber++;
    }
    
    public Material(String name, float[] ambient, float[] diffuse, float[] specular, float[] emissive, float shininess, float transparency, Texture texture, String alphaMap) {
        setName(name);
        setAmbient(ambient);
        setDiffuse(diffuse);
        setSpecular(specular);
        setEmissive(emissive);
        setShininess(shininess);
        setTransparency(transparency);
        setTexture(texture);
        setAlphaMap(alphaMap);
        
        materialNumber++;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getAmbient() {
        return ambient;
    }
    
    public void setAmbient(float[] ambient) {
        this.ambient = (ambient.length == 4 ? ambient : DEFAULT_AMBIENT);
    }
    
    public void setAmbient(float f1, float f2, float f3, float f4) {
        this.ambient = new float[]{ f1, f2, f3, f4 };
    }
    
    public float[] getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(float[] diffuse) {
        this.diffuse = (diffuse.length == 4 ? diffuse : DEFAULT_DIFFUSE);
    }

    public void setDiffuse(float f1, float f2, float f3, float f4) {
        this.diffuse = new float[]{f1, f2, f3, f4};
    }

    public float[] getSpecular() {
        return specular;
    }

    public void setSpecular(float[] specular) {
        this.specular = (specular.length == 4 ? specular : DEFAULT_SPECULAR);
    }

    public void setSpecular(float f1, float f2, float f3, float f4) {
        this.specular = new float[]{f1, f2, f3, f4};
    }

    public float[] getEmissive() {
        return emissive;
    }

    public void setEmissive(float[] emissive) {
        this.emissive = (emissive.length == 4 ? emissive : DEFAULT_EMISSIVE);
    }

    public void setEmissive(float f1, float f2, float f3, float f4) {
        this.emissive = new float[]{f1, f2, f3, f4};
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        if (shininess < 0 || shininess > 128) shininess = DEFAULT_SHININESS;
        this.shininess = shininess;
    }

    public float getTransparency() {
        return transparency;
    }

    public void setTransparency(float transparency) {
        if (transparency < 0.0f || transparency > 1.0f) transparency = DEFAULT_TRANSPARENCY;
        this.transparency = transparency;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getAlphaMap() {
        return alphaMap;
    }

    public void setAlphaMap(String alphaMap) {
        this.alphaMap = alphaMap;
    }

    @Override
    public String toString() {
        return "Name: \"" + getName() + "\", ambient: " + Arrays.toString(getAmbient()) + ", diffuse: " + Arrays.toString(getDiffuse()) +
                ", specular: " + Arrays.toString(getSpecular()) + ", emissive: " + Arrays.toString(getEmissive()) +
                ", shininess: " + getShininess() + ", transparency: " + getTransparency() +
                ", color map: \"" + getTexture() + "\", alpha map: \"" + getAlphaMap() + "\"";
    }

}
