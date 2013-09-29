package opengl.engine;

import com.jogamp.opengl.util.texture.Texture;
import opengl.model.Material;
import opengl.model.Mesh;
import opengl.model.Model;

import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import java.io.IOException;

public class ModelCompiler {
    private final GL2 gl;

    public ModelCompiler(final GL2 gl) {
        this.gl = gl;
    }

    public void compile(Model model) {
        if (model.getMeshes().length > 0) {
            Texture texture = null;

            int displayList = gl.glGenLists(model.getMeshes().length);
            model.setDisplayList(displayList);

            int i = 0;
            for (Mesh mesh : model.getMeshes()) {
                int materialIndex = mesh.getMaterialIndex();
                Material material = materialIndex >= 0 ? model.getMaterials()[materialIndex] : null;

                gl.glNewList(displayList + i++, GL2.GL_COMPILE);

                if (material != null) {
                    texture = material.getTexture();

                    if (texture != null) {
                        texture.enable(gl);
                        texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
                        texture.setTexParameterf(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
                        //gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
                        texture.bind(gl);
                    }

                    gl.glMaterialfv(GL2.GL_FRONT, GLLightingFunc.GL_AMBIENT, material.getAmbient(), 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GLLightingFunc.GL_DIFFUSE, material.getDiffuse(), 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GLLightingFunc.GL_SPECULAR, material.getSpecular(), 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GLLightingFunc.GL_EMISSION, material.getEmissive(), 0);
                    gl.glMaterialf(GL2.GL_FRONT, GLLightingFunc.GL_SHININESS, material.getShininess());
                }

                int[][] triangles = mesh.getTriangles();
                float[][] vertices = mesh.getVertices();
                float[][] normals = mesh.getNormals();

                gl.glBegin(GL2.GL_TRIANGLES);
                for (int j = 0; j < triangles.length; j++) {
                    for (int k = 0; k < 3; k++) {
                        gl.glNormal3fv(normals[triangles[j][k + 4]], 0);
                        gl.glTexCoord2f(vertices[triangles[j][k + 1]][4], vertices[triangles[j][k + 1]][5]);
                        gl.glVertex3fv(vertices[triangles[j][k + 1]], 1);
                    }
                }
                gl.glEnd();

                if (texture != null) {
                    texture.disable(gl);
                }

                gl.glEndList();
            }
        }
    }

}
