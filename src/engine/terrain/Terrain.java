package engine.terrain;

import engine.rendering.utility.Texture;
import org.joml.Vector3f;

public class Terrain {
    private TerrainGenerator terrainGenerator;
    private Vector3f position;

    public Terrain(Vector3f position, float size) {
        this.position = position;
        terrainGenerator = new TerrainGenerator(size, new Texture("res/textures/stallTexture.png"));
    }

    public Vector3f getPosition() {
        return position;
    }

    public TerrainGenerator getTerrainGenerator() {
        return terrainGenerator;
    }
}
