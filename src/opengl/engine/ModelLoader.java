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
            String nullOut = null;

            nullOut = th.nextString("block");
            model.setBlock(th.nextInt("block") != 0);
            nullOut = th.nextString("frames variable");
            int numFrames = th.nextInt("number of frames");
            nullOut = th.nextString("frame variable");
            int frame = th.nextInt("current frame number");
            nullOut = th.nextString("meshes variable");

            // Wczytywanie grup (meshes - kawalki obiektu o roznych materialach)
            int numMeshes = th.nextInt("number of meshes");
            Mesh[] meshes = new Mesh[numMeshes];
            for (int mesh = 0; mesh < numMeshes; mesh++) {
                String meshName = th.nextQuote("name of mesh");
                //System.out.print("    Mesh " + mesh + ": ");
                int meshFlags = th.nextInt("current mesh flags");
                int meshMaterialIndex = th.nextInt("current mesh material index");

                // Wczytywanie punktow
                int numVertices = th.nextInt("number of vertices");
                float[][] vertices = new float[numVertices][7];
                for (int i = 0; i < numVertices; i++) {
                    for (int j = 0; j < 7; j++) {
                        vertices[i][j] = th.nextFloat("vertex");
                    }
                }

                // Wczytywanie wektorow normalnych
                int numNormals = th.nextInt("number of normals");
                float[][] normals = new float[numNormals][3];
                for (int i = 0; i < numNormals; i++) {
                    for (int j = 0; j < 3; j++) {
                        normals[i][j] = th.nextFloat("normal");
                    }
                }

                // Wczytywanie trojkatow
                int numTriangles = th.nextInt("number of triangles");
                int[][] triangles = new int[numTriangles][8];
                for (int i = 0; i < numTriangles; i++) {
                    for (int j = 0; j < 8; j++) {
                        triangles[i][j] = th.nextInt("triangle");
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
            nullOut = th.nextString("materials variable");
            int numMaterials = th.nextInt("number of materials");
            Material[] materials = new Material[numMaterials];
            for (int material = 0; material < numMaterials; material++) {
                String materialName = th.nextQuote("name of material");
                //System.out.print("    Material " + material + ": ");
                float[] ambient = new float[4];
                for (int i = 0; i < 4; i++) {
                    ambient[i] = th.nextFloat("ambient");
                }

                float[] diffuse = new float[4];
                for (int i = 0; i < 4; i++) {
                    diffuse[i] = th.nextFloat("diffuse");
                }

                float[] specular = new float[4];
                for (int i = 0; i < 4; i++) {
                    specular[i] = th.nextFloat("specular");
                }

                float[] emissive = new float[4];
                for (int i = 0; i < 4; i++) {
                    emissive[i] = th.nextFloat("emissive");
                }

                float shininess = th.nextFloat("shininess");
                float transparency = th.nextFloat("transparency");
                String textureName = th.nextQuote("color map");
                String alphaMap = th.nextQuote("alpha map");

                Texture texture = null;
                if (!textureName.isEmpty()) {
                    texture = textureRepository.get(textureName);
                }

                materials[material] = new Material(materialName, ambient, diffuse, specular, emissive, shininess, transparency, texture, alphaMap);
            }

            // Wczytywanie informacji o animacji
            nullOut = th.nextString("animated variable");
            String isAnimated = th.nextString("is model animated");
            if ("true".equals(isAnimated)) {
                float[] animationDelta = new float[3];
                animationDelta[0] = th.nextFloat("angleX");
                animationDelta[1] = th.nextFloat("angleY");
                animationDelta[2] = th.nextFloat("angleZ");
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
