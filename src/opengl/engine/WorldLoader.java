/*
 * MapHelper.java
 *
 * Created on 28 maj 2006, 22:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package opengl.engine;

import opengl.model.Model;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry;

/**
 *
 * @author mixcherry
 */
public class WorldLoader implements ResourceLoader<World> {
    private ResourceRepository<Model> modelRepository;

    public WorldLoader(ResourceRepository<Model> modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Override
    public World load(String path) throws IOException {
        InputStream worldStream = null;
        World world = null;

        try {
            worldStream = this.getClass().getClassLoader().getResourceAsStream(Config.MAPS_PATH + path);

            if (worldStream == null) {
                throw new IOException("Resource \"" + path + "\" not found");
            }

            Reader reader = new BufferedReader(new InputStreamReader(worldStream));
            StreamTokenizer tokenizer = new StreamTokenizer(reader);

            ResourceTokenizer rt = new ResourceTokenizer(tokenizer);
            ResourceTokenizer.setupTokenizer(tokenizer);

            List<String> worldLayers = new ArrayList<String>();
            Map<String, String> modelMap = new HashMap<String, String>();
            Map<Point, float[][]> lightMap = new HashMap<Point, float[][]>();
            Point start = null;

            String token = rt.nextToken("token");
            while (!rt.eof()) {
                if (token.equalsIgnoreCase("Layer:")) {
                    StringBuilder sb = new StringBuilder();
                    int width = 0, height = 0;

                    String mapLine = rt.nextQuote("map string");
                    while (!mapLine.isEmpty()) {
                        width = Math.max(width, mapLine.length());
                        height += 1;

                        sb.append(mapLine);

                        mapLine = rt.nextQuote("map string");
                    }

                    if (world == null) {
                        world = new World(width, height);
                    }

                    worldLayers.add(sb.toString());
                } else if (token.equalsIgnoreCase("Model:")) {
                    modelMap.put(rt.nextString("Model symbol"), rt.nextString("Model name"));
                } else if (token.equalsIgnoreCase("Light:")) {
                    Point lightPosition = new Point();
                    lightPosition.x = rt.nextInt("x-coordinate of light position");
                    lightPosition.y = rt.nextInt("y-coordinate of light position");

                    float[] ambient = new float[4];
                    for (int i=0; i<4; i++) ambient[i] = rt.nextFloat("ambient");

                    float[] diffuse = new float[4];
                    for (int i=0; i<4; i++) diffuse[i] = rt.nextFloat("diffuse");

                    float[] specular = new float[4];
                    for (int i=0; i<4; i++) specular[i] = rt.nextFloat("specular");

                    float[] position = new float[4];
                    for (int i=0; i<4; i++) position[i] = rt.nextFloat("position");

                    lightMap.put(lightPosition, new float[][]{ambient, diffuse, specular, position});
                } else if (token.equalsIgnoreCase("Start:")) {
                    start = new Point();

                    start.x = rt.nextInt("x-coordinate of starting position");
                    start.y = rt.nextInt("y-coordinate of starting position");
                }

                token = rt.nextToken("token");
            }

            for (String worldLayer : worldLayers) {
                for (int i = 0; i < world.getHeight(); i++) {
                    for (int j = 0; j < world.getWidth(); j++) {
                        char ch = worldLayer.charAt(i * world.getWidth() + j);

                        if (ch != ' ') {
                            world.addModel(modelRepository.get(modelMap.get(String.valueOf(ch))), j, i);
                        }
                    }
                }
            }

            int i = 0;
            for (Entry<Point, float[][]> entry : lightMap.entrySet()) {
                Point position = entry.getKey();

                world.getLightMap()[position.y][position.x] = i;
                world.getLights()[i] = entry.getValue();

                i++;
            }

            world.setStart(start);
        } finally {
            if (worldStream != null) {
                worldStream.close();
            }
        }

        return world;
    }

}
