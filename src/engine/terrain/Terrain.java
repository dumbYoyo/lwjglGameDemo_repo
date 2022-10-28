package engine.terrain;

import org.joml.Vector3f;

public class Terrain {
    private TerrainGenerator terrainGenerator;
    private Vector3f position;

    public Terrain(Vector3f position) {
        this.position = position;
        terrainGenerator = new TerrainGenerator();
    }

    public Vector3f getPosition() {
        return position;
    }

    public TerrainGenerator getTerrainGenerator() {
        return terrainGenerator;
    }
}
