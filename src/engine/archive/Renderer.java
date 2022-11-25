package engine.archive;

import engine.archive.IHud;
import engine.objects.Camera;
import engine.objects.Entity;
import engine.Window;
import engine.math.Mathf;
import engine.objects.Light;
import engine.rendering.utility.Mesh;
import engine.rendering.utility.Shader;
import engine.rendering.utility.Texture;
import engine.terrain.Terrain;
import engine.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Renderer {
    private List<Entity> entities = new ArrayList<>();
    private Mathf matrixHelper;

    private Camera camera;

    private Shader hudShader;
    private Texture hudTexture;

    public Renderer(Camera camera) {
        this.camera = camera;
        matrixHelper = new Mathf();
        hudShader = new Shader("res/shaders/text/vertex.vs", "res/shaders/text/fragment.fs");
        hudShader.compile();
        hudTexture = new Texture("res/text/verdana.png");
    }

    /*
    public void render(Window window, List<Entity> entities, Shader shader, Light light, Texture texture) {
        this.entities = entities;
        shader.bind();
        Matrix4f proj = matrixHelper.getProjMatrix(45, window.getWidth() / (float) window.getHeight(), 0.1f, 1000f);
        shader.loadMatrix4f("proj", proj);
        shader.loadInteger("tex_sampler", 0);
        GL20.glActiveTexture(GL13.GL_TEXTURE0);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        Matrix4f view = matrixHelper.getViewMatrix(camera);
        shader.loadMatrix4f("view", view);
        shader.loadVec3f("lightPos", light.getPosition());
        shader.loadVec3f("lightColor", light.getColor());
        for (Entity entity : entities) {
            Matrix4f model = matrixHelper.getModelMatrix(entity);
            shader.loadMatrix4f("model", model);
            // Matrix4f modelView = matrixHelper.getModelViewMatrix(gameObject, view);
            // shader.loadMatrix4f("modelView", modelView);
            shader.loadFloat("material.specularStrength", entity.getSpecularStrength());
            shader.loadFloat("material.reflectivity", entity.getReflectivity());
            if (entity.getEntityData().getMesh() != null) {
                entity.getEntityData().getMesh().render();
            }
            if (entity.getEntityData().getMeshes() != null) {
                for (int i = 0; i < entity.getEntityData().getMeshes().length; i++) {
                    entity.getEntityData().getMeshes()[i].render();
                }
            }
        }
        shader.unbind();
    }

    public void render(Window window, Entity entity, Shader shader, Light light, Texture texture) {
        this.entities.add(entity);
        shader.bind();
        Matrix4f proj = matrixHelper.getProjMatrix(45, window.getWidth() / (float) window.getHeight(), 0.1f, 1000f);
        shader.loadMatrix4f("proj", proj);
        shader.loadInteger("tex_sampler", 0);
        GL20.glActiveTexture(GL13.GL_TEXTURE0);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        Matrix4f view = matrixHelper.getViewMatrix(camera);
        shader.loadMatrix4f("view", view);
        Matrix4f model = matrixHelper.getModelMatrix(entity);
        shader.loadMatrix4f("model", model);
        // Matrix4f modelView = matrixHelper.getModelViewMatrix(gameObject, view);
        // shader.loadMatrix4f("modelView", modelView);
        shader.loadVec3f("lightPos", light.getPosition());
        shader.loadVec3f("lightColor", light.getColor());
        shader.loadFloat("material.specularStrength", entity.getSpecularStrength());
        shader.loadFloat("material.reflectivity", entity.getReflectivity());
        if (entity.getEntityData().getMesh() != null) {
            entity.getEntityData().getMesh().render();
        }
        if (entity.getEntityData().getMeshes() != null) {
            for (int i = 0; i < entity.getEntityData().getMeshes().length; i++) {
                entity.getEntityData().getMeshes()[i].render();
            }
        }
        shader.unbind();
    }
    */

    public void render(Terrain terrain, Shader shader, Texture texture, Light light) {
        shader.bind();

        Matrix4f proj = matrixHelper.getProjMatrix(45, (float) Window.width / (float) Window.height, 0.1f, 1000f);
        shader.loadMatrix4f("proj", proj);
        shader.loadInteger("tex_sampler", 0);
        GL20.glActiveTexture(GL13.GL_TEXTURE0);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        Matrix4f view = matrixHelper.getViewMatrix(camera);
        shader.loadMatrix4f("view", view);
        Matrix4f model = matrixHelper.getModelMatrix(new Vector3f(terrain.getPosition().x - terrain.getTerrainGenerator().getSize()/2f, terrain.getPosition().y, terrain.getPosition().y - terrain.getTerrainGenerator().getSize()/2f), new Vector3f(0, 0, 0), new Vector3f(1));
        shader.loadMatrix4f("model", model);
        shader.loadVec3f("lightPos", light.getPosition());
        shader.loadVec3f("lightColor", light.getColor());

        terrain.getTerrainGenerator().getMesh().render();

        shader.unbind();
    }

    public void render(IHud hud) {
        hudShader.bind();
        Matrix4f ortho = matrixHelper.getOrthoMatrix(0, Window.width, Window.height, 0);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, hudTexture.getTextureID());
        for (Entity entity : hud.getGameObjects()) {
            Mesh mesh = entity.getEntityData().getMesh();
            Matrix4f orthoModel = matrixHelper.getOrthoModelMatrix(entity, ortho);
            hudShader.loadVec4f("color", entity.getColor());
            hudShader.loadMatrix4f("orthoModel", orthoModel);

            mesh.render();
        }
        hudShader.unbind();
    }

    public void render(Text text) {
        hudShader.bind();

        GL20.glBindTexture(GL11.GL_TEXTURE_2D, hudTexture.getTextureID());

        Matrix4f ortho = matrixHelper.getOrthoMatrix(0, Window.width, Window.height, 0);
        hudShader.loadMatrix4f("proj", ortho);
        Matrix4f model = matrixHelper.getModelMatrix(new Vector3f(text.getPosition(), 0), new Vector3f(0, 0, 0), new Vector3f(text.getScale(), 1));
        hudShader.loadMatrix4f("model", model);

        text.getMesh().render();

        hudShader.unbind();
    }

    public void clear() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 1);
    }

    public void cleanUp() {
        for (Entity entity : entities) {
            if (entity.getEntityData().getMesh() != null) {
                entity.getEntityData().getMesh().cleanUp();
            }
            /*
            if (entity.getEntityData().getMeshes() != null) {
                for (int i = 0; i < entity.getEntityData().getMeshes().length; i++) {
                    entity.getEntityData().getMeshes()[i].cleanUp();
                }
            }
             */
        }
    }
}
