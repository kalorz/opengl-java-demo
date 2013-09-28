/*
 * ModelLoader.java
 *
 * Created on February 6, 2006, 4:25 AM
 */
package opengl.engine;

import com.jogamp.opengl.util.texture.Texture;
import opengl.model.Material;
import opengl.model.Mesh;
import opengl.model.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;

/**
 *
 * @author mistrz
 */
public class ModelLoader implements ResourceLoader<Model> {
    private ResourceRepository<Texture> textureRepository;

    public ModelLoader(ResourceRepository<Texture> textureRepository) {
        this.textureRepository = textureRepository;
    }

    @Override
    public Model load(String path) throws IOException {
        InputStream modelStream = null;
        Model model = null;

        try {
            modelStream = this.getClass().getClassLoader().getResourceAsStream(Config.MODELS_PATH + path);

            if (modelStream == null) {
                throw new IOException("Resource \"" + path + "\" not found");
            }

            Reader reader = new BufferedReader(new InputStreamReader(modelStream));
            StreamTokenizer tokenizer = new StreamTokenizer(reader);

            ResourceTokenizer th = new ResourceTokenizer(tokenizer);
            ResourceTokenizer.setupTokenizer(tokenizer);

            model = new Model();

            System.out.println("Loading model " + path);

            th.nextString();
            model.setBlock(th.nextInt() != 0);

            th.nextString();
            th.nextInt();
            th.nextString();
            th.nextInt();
            th.nextString();

            // Wczytywanie grup (meshes - kawalki obiektu o roznych materialach)
            int numMeshes = th.nextInt();
            Mesh[] meshes = new Mesh[numMeshes];
            for (int mesh = 0; mesh < numMeshes; mesh++) {
                String meshName = th.nextQuote();
                //System.out.print("    Mesh " + mesh + ": ");
                int meshFlags = th.nextInt();
                int meshMaterialIndex = th.nextInt();

                // Wczytywanie punktow
                int numVertices = th.nextInt();
                float[][] vertices = new float[numVertices][7];
                for (int i = 0; i < numVertices; i++) {
                    for (int j = 0; j < 7; j++) {
                        vertices[i][j] = th.nextFloat();
                    }
                }

                // Wczytywanie wektorow normalnych
                int numNormals = th.nextInt();
                float[][] normals = new float[numNormals][3];
                for (int i = 0; i < numNormals; i++) {
                    for (int j = 0; j < 3; j++) {
                        normals[i][j] = th.nextFloat();
                    }
                }

                // Wczytywanie trojkatow
                int numTriangles = th.nextInt();
                int[][] triangles = new int[numTriangles][8];
                for (int i = 0; i < numTriangles; i++) {
                    for (int j = 0; j < 8; j++) {
                        triangles[i][j] = th.nextInt();
                    }
                }

                meshes[mesh] = new Mesh();
                meshes[mesh].setName(meshName);
                meshes[mesh].setVertices(vertices);
                meshes[mesh].setNormals(normals);
                meshes[mesh].setTriangles(triangles);
                meshes[mesh].setMaterialIndex(meshMaterialIndex);
            }

            // Wczytywanie materialow
            th.nextString();
            int numMaterials = th.nextInt();
            Material[] materials = new Material[numMaterials];
            for (int material = 0; material < numMaterials; material++) {
                String materialName = th.nextQuote();
                //System.out.print("    Material " + material + ": ");
                float[] ambient = new float[4];
                for (int i = 0; i < 4; i++) {
                    ambient[i] = th.nextFloat();
                }

                float[] diffuse = new float[4];
                for (int i = 0; i < 4; i++) {
                    diffuse[i] = th.nextFloat();
                }

                float[] specular = new float[4];
                for (int i = 0; i < 4; i++) {
                    specular[i] = th.nextFloat();
                }

                float[] emissive = new float[4];
                for (int i = 0; i < 4; i++) {
                    emissive[i] = th.nextFloat();
                }

                float shininess = th.nextFloat();
                float transparency = th.nextFloat();
                String textureName = th.nextQuote();
                String alphaMap = th.nextQuote();

                Texture texture = null;
                if (!textureName.isEmpty()) {
                    texture = textureRepository.get(textureName);
                }

                materials[material] = new Material(materialName, ambient, diffuse, specular, emissive, shininess, transparency, texture, alphaMap);
            }

            // Wczytywanie informacji o animacji
            th.nextString();
            String isAnimated = th.nextString();
            if ("true".equals(isAnimated)) {
                float[] animationDelta = new float[3];
                animationDelta[0] = th.nextFloat();
                animationDelta[1] = th.nextFloat();
                animationDelta[2] = th.nextFloat();
                model.animationDelta = animationDelta;
                model.animation = new float[3];
                model.animated = true;
            }
            reader.close();

            model.setMeshes(meshes);
            model.setMaterials(materials);
        } finally {
            if (modelStream != null) {
                modelStream.close();
            }
        }

        return model;
    }
}
