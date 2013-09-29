/*
 * MapHelper.java
 *
 * Created on 28 maj 2006, 22:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package opengl.resources;

import opengl.engine.Config;
import opengl.engine.World;
import opengl.model.Model;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

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
            Map<String, String> modelMap = new LinkedHashMap<String, String>();
            Map<Point, float[][]> lightMap = new LinkedHashMap<Point, float[][]>();
            Point start = null;

            String token = rt.nextToken();
            while (!rt.eof()) {
                if (token.equalsIgnoreCase("Layer:")) {
                    StringBuilder sb = new StringBuilder();
                    int width = 0, height = 0;

                    String mapLine = rt.nextQuote();
                    while (!mapLine.isEmpty()) {
                        width = Math.max(width, mapLine.length());
                        height += 1;

                        sb.append(mapLine);

                        mapLine = rt.nextQuote();
                    }

                    if (world == null) {
                        world = new World(width, height);
                    }

                    worldLayers.add(sb.toString());
                } else if (token.equalsIgnoreCase("Model:")) {
                    modelMap.put(rt.nextString(), rt.nextString());
                } else if (token.equalsIgnoreCase("Light:")) {
                    Point lightPosition = new Point();
                    lightPosition.x = rt.nextInt();
                    lightPosition.y = rt.nextInt();

                    float[] ambient = new float[4];
                    for (int i=0; i<4; i++) ambient[i] = rt.nextFloat();

                    float[] diffuse = new float[4];
                    for (int i=0; i<4; i++) diffuse[i] = rt.nextFloat();

                    float[] specular = new float[4];
                    for (int i=0; i<4; i++) specular[i] = rt.nextFloat();

                    float[] position = new float[4];
                    for (int i=0; i<4; i++) position[i] = rt.nextFloat();

                    lightMap.put(lightPosition, new float[][]{ambient, diffuse, specular, position});
                } else if (token.equalsIgnoreCase("Start:")) {
                    start = new Point();

                    start.x = rt.nextInt();
                    start.y = rt.nextInt();
                }

                token = rt.nextToken();
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
