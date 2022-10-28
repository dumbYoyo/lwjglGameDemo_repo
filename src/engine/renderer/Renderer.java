package engine.renderer;

import engine.IHud;
import engine.objects.Camera;
import engine.objects.GameObject;
import engine.Window;
import engine.math.uMatrix;
import engine.objects.Light;
import engine.terrain.Terrain;
import engine.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private List<GameObject> gameObjects = new ArrayList<>();
    private uMatrix matrixHelper;

    private Camera camera;

    private Shader hudShader;
    private Texture hudTexture;

    public Renderer(Camera camera) {
        this.camera = camera;
        matrixHelper = new uMatrix();
        hudShader = new Shader("res/shaders/text/vertex.vs", "res/shaders/text/fragment.fs");
        hudShader.compile();
        hudTexture = new Texture("res/text/verdana.png");
    }

    public void render(Window window, List<GameObject> gameObjects, Shader shader, Light light, Texture texture) {
        this.gameObjects = gameObjects;
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
        for (GameObject gameObject : gameObjects) {
            Matrix4f model = matrixHelper.getModelMatrix(gameObject);
            shader.loadMatrix4f("model", model);
            // Matrix4f modelView = matrixHelper.getModelViewMatrix(gameObject, view);
            // shader.loadMatrix4f("modelView", modelView);
            shader.loadFloat("material.specularStrength", gameObject.getSpecularStrength());
            shader.loadFloat("material.reflectivity", gameObject.getReflectivity());
            if (gameObject.getMesh() != null) {
                gameObject.getMesh().render();
            }
            if (gameObject.getMeshes() != null) {
                for (int i = 0; i < gameObject.getMeshes().length; i++) {
                    gameObject.getMeshes()[i].render();
                }
            }
        }
        shader.unbind();
    }

    public void render(Window window, GameObject gameObject, Shader shader, Light light, Texture texture) {
        this.gameObjects.add(gameObject);
        shader.bind();
        Matrix4f proj = matrixHelper.getProjMatrix(45, window.getWidth() / (float) window.getHeight(), 0.1f, 1000f);
        shader.loadMatrix4f("proj", proj);
        shader.loadInteger("tex_sampler", 0);
        GL20.glActiveTexture(GL13.GL_TEXTURE0);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        Matrix4f view = matrixHelper.getViewMatrix(camera);
        shader.loadMatrix4f("view", view);
        Matrix4f model = matrixHelper.getModelMatrix(gameObject);
        shader.loadMatrix4f("model", model);
        // Matrix4f modelView = matrixHelper.getModelViewMatrix(gameObject, view);
        // shader.loadMatrix4f("modelView", modelView);
        shader.loadVec3f("lightPos", light.getPosition());
        shader.loadVec3f("lightColor", light.getColor());
        shader.loadFloat("material.specularStrength", gameObject.getSpecularStrength());
        shader.loadFloat("material.reflectivity", gameObject.getReflectivity());
        if (gameObject.getMesh() != null) {
            gameObject.getMesh().render();
        }
        if (gameObject.getMeshes() != null) {
            for (int i = 0; i < gameObject.getMeshes().length; i++) {
                gameObject.getMeshes()[i].render();
            }
        }
        shader.unbind();
    }

    public void render(Terrain terrain, Shader shader, Texture texture, Light light) {
        shader.bind();

        Matrix4f proj = matrixHelper.getProjMatrix(45, (float) Window.width / (float) Window.height, 0.1f, 1000f);
        shader.loadMatrix4f("proj", proj);
        shader.loadInteger("tex_sampler", 0);
        GL20.glActiveTexture(GL13.GL_TEXTURE0);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        Matrix4f view = matrixHelper.getViewMatrix(camera);
        shader.loadMatrix4f("view", view);
        Matrix4f model = matrixHelper.getModelMatrix(terrain.getPosition(), new Vector3f(0, 0, 0), new Vector3f(1));
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
        for (GameObject gameObject : hud.getGameObjects()) {
            Mesh mesh = gameObject.getMesh();
            Matrix4f orthoModel = matrixHelper.getOrthoModelMatrix(gameObject, ortho);
            hudShader.loadVec4f("color", gameObject.getColor());
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
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getMesh() != null) {
                gameObject.getMesh().cleanUp();
            }
            if (gameObject.getMeshes() != null) {
                for (int i = 0; i < gameObject.getMeshes().length; i++) {
                    gameObject.getMeshes()[i].cleanUp();
                }
            }
        }
    }
}
