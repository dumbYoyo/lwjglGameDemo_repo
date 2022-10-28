package engine.terrain;

import org.joml.Vector3f;

public class Terrain {
    private TerrainGenerator terrainGenerator;
    private Vector3f position = new Vector3f(0, 0, 0);

    public Terrain() {
        terrainGenerator = new TerrainGenerator();
    }

    public Vector3f getPosition() {
        return position;
    }

    public TerrainGenerator getTerrainGenerator() {
        return terrainGenerator;
    }
}
