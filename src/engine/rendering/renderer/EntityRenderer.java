package engine.rendering.renderer;

import engine.math.Mathf;
import engine.objects.Entity;
import engine.objects.EntityData;
import engine.rendering.utility.Mesh;
import engine.rendering.utility.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class EntityRenderer {
    private Shader shader;
    private Mathf mathf;

    public EntityRenderer(Shader shader) {
        this.shader = shader;
        mathf = new Mathf();
    }

    public void render(Map<EntityData, List<Entity>> entities) {
        for (EntityData entityData : entities.keySet()) {
            for (Mesh mesh : entityData.getMeshes()) {
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL20.glBindTexture(GL11.GL_TEXTURE_2D, entityData.getTexture().getTextureID());

                GL30.glBindVertexArray(mesh.getVao());
                GL20.glEnableVertexAttribArray(0);
                GL20.glEnableVertexAttribArray(1);
                GL20.glEnableVertexAttribArray(2);

                List<Entity> batch = entities.get(entityData);
                for (Entity entity : batch) {
                    Matrix4f model = mathf.getModelMatrix(entity.getPosition(), entity.getRotation(), new Vector3f(entity.getScale()));
                    shader.loadMatrix4f("model", model);
                    shader.loadFloat("material.specularStrength", entity.getSpecularStrength());
                    shader.loadFloat("material.reflectivity", entity.getReflectivity());

                    GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getEntityData().getMesh().vertexCount, GL11.GL_UNSIGNED_INT, 0);
                }

                GL20.glDisableVertexAttribArray(0);
                GL20.glDisableVertexAttribArray(1);
                GL20.glDisableVertexAttribArray(2);
                GL30.glBindVertexArray(0);
            }
        }
    }
}
