package engine.rendering.renderer;

import engine.objects.Mesh;
import engine.rendering.Shader;
import engine.utility.Mathf;
import engine.objects.Entity;
import engine.objects.EntityData;
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

    public EntityRenderer(Shader shader) {
        this.shader = shader;
    }

    public void render(Map<EntityData, List<Entity>> entities, Entity player) {
        for (EntityData entityData : entities.keySet()) {
            for (Mesh mesh : entityData.getMeshes()) {
                shader.loadInteger("tex_sampler", 0);
                if (entityData.getTexture() != null) {
                    GL13.glActiveTexture(GL13.GL_TEXTURE0);
                    GL20.glBindTexture(GL11.GL_TEXTURE_2D, entityData.getTexture().getTextureID());
                }

                GL30.glBindVertexArray(mesh.getVao());
                GL20.glEnableVertexAttribArray(0);
                GL20.glEnableVertexAttribArray(1);
                GL20.glEnableVertexAttribArray(2);

                List<Entity> batch = entities.get(entityData);
                for (Entity entity : batch) {
                    Matrix4f lightProj = new Matrix4f().identity();
                    lightProj.ortho(-15, 15, -15, 15, -15, 15);

                    Matrix4f lightView = new Matrix4f().identity();
                    lightView.lookAt(new Vector3f(-2.0f, 4.0f, -1.0f), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
                    lightView.translate(new Vector3f(player.getPosition()).negate());

                    Matrix4f lightSpace = lightProj.mul(lightView);

                    Matrix4f model = Mathf.getModelMatrix(entity.getPosition(), entity.getRotation(), new Vector3f(entity.getScale()));
                    shader.loadMatrix4f("model", model);
                    shader.loadFloat("material.specularStrength", entity.getSpecularStrength());
                    shader.loadFloat("material.reflectivity", entity.getReflectivity());
                    shader.loadVec3f("material.tint", entity.getTint());
                    shader.loadVec3f("material.color", entity.getColor());
                    shader.loadMatrix4f("lightSpace", lightSpace);

                    GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getEntityData().getMesh().vertexCount, GL11.GL_UNSIGNED_INT, 0);
                }

                GL20.glDisableVertexAttribArray(0);
                GL20.glDisableVertexAttribArray(1);
                GL20.glDisableVertexAttribArray(2);
                GL30.glBindVertexArray(0);
            }
        }
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
