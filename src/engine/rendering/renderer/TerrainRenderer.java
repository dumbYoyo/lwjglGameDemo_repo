package engine.rendering.renderer;

import engine.math.Mathf;
import engine.rendering.utility.Shader;
import engine.terrain.Terrain;
import engine.terrain.TerrainGenerator;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class TerrainRenderer {
    private Shader shader;
    private Mathf mathf;

    public TerrainRenderer(Shader shader) {
        this.shader = shader;
        mathf = new Mathf();
    }

    public void render(Map<TerrainGenerator, List<Terrain>> terrains) {
        for (TerrainGenerator terrainData : terrains.keySet()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL20.glBindTexture(GL11.GL_TEXTURE_2D, terrainData.getTexture().getTextureID());

            GL30.glBindVertexArray(terrainData.getMesh().getVao());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);

            List<Terrain> batch = terrains.get(terrainData);
            for (Terrain terrain : batch) {
                Matrix4f model = mathf.getModelMatrix(new Vector3f(terrain.getPosition().x - terrain.getTerrainGenerator().getSize()/2f, terrain.getPosition().y, terrain.getPosition().y - terrain.getTerrainGenerator().getSize()/2f), new Vector3f(0, 0, 0), new Vector3f(1));
                shader.loadMatrix4f("model", model);

                GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getTerrainGenerator().getMesh().vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
        }
    }
}
