/*
 * Maze.java
 *
 * Created on 24 maj 2006, 00:05
 */
package opengl.engine;

import opengl.model.Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Karolek
 */
public class World {
    public static final int MAX_LIGHTS = 6;
    
    private float[][][] lights = new float[MAX_LIGHTS][4][4];
    private Point       start;
    private int         width;
    private int         height;
    private List<Model>[][] map;
    private int[][]     lightMap;

    public World(int width, int height) {
        this.width  = Math.max(width, 1);
        this.height = Math.max(height, 1);

        map      = new List[height][width];
        lightMap = new int[height][width];
        for (int[] row : lightMap) {
            Arrays.fill(row, -1);
        }
    }

    public World() {
        this(1, 1);
    }

    public void addModel(Model model, int x, int y) {
        if (map[y][x] == null) {
            map[y][x] = new ArrayList<Model>();
        }

        map[y][x].add(model);
    }

    public List<Model> getModels(int x, int y) {
        return map[y][x];
    }

    public boolean isBlock(int x, int y) {
        List<Model> models = getModels(x, y);

        if (models != null && models.size() > 0) {
            for (Model model : models) {
                if (model.isBlock()) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }
    
    public float[][][] getLights() {
        return lights;
    }

    public int[][] getLightMap() {
        return lightMap;
    }
    
}
