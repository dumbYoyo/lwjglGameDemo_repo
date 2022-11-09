package game;

import engine.*;
import engine.input.MouseInput;
import engine.input.MouseListener;
import engine.objects.Camera;
import engine.objects.Entity;
import engine.objects.Light;
import engine.rendering.Renderer;
import engine.rendering.renderer.MasterRenderer;
import engine.rendering.utility.Texture;
import engine.terrain.Terrain;
import engine.text.Text;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class TestGame implements IGameLogic {
    private Entity entity;
    private Camera camera;
    private Light light;
    private Terrain terrain;
    private MasterRenderer renderer;
    private Entity player;

    private Text text;
    private int score = 0;

    @Override
    public void init() {
        camera = new Camera();
        camera.setPosition(0, -2, -5);

        light = new Light(new Vector3f(2, 10, -2), new Vector3f(1, 1, 1));

        renderer = new MasterRenderer(camera);

        entity = new Entity(ModelLoader.load("res/models/dragon.obj"));
        entity.setPosition(0, 0, -10);
        entity.setRotation(0, 180, 0);
        entity.setSpecularStrength(128);
        entity.setReflectivity(1);
        entity.getEntityData().setTexture(new Texture("res/textures/ball.png"));

        text = new Text("Hello World", new Vector2f(20, Window.height-690), new Vector2f(0.5f));

        terrain = new Terrain(new Vector3f(0, 0, 0), 100);

        player = new Entity(ModelLoader.load("res/models/ball.obj"));
        player.setPosition(10, 0, 0);
        player.getEntityData().setTexture(new Texture("res/textures/ball.png"));
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
    }

    @Override
    public void update(Window window, float dt, MouseInput mouseInput) {
        entity.getRotation().y += 3 * dt;

        cameraMovement(window, dt);
    }

    @Override
    public void render(Window window) {
        Window.clear();

        renderer.addEntity(entity);
        renderer.addTerrain(terrain);
        renderer.addText(text);
        renderer.addEntity(player);

        renderer.render(light);
    }

    float distance = 50;
    private void cameraMovement(Window window, float dt) {
        float cameraMoveSpeed = 5;
        float cameraRotateSpeed = 3;
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            camera.getPosition().z -= cameraMoveSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            camera.getPosition().z += cameraMoveSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            camera.getPosition().x += cameraMoveSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            camera.getPosition().x -= cameraMoveSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
            camera.getPosition().y += cameraMoveSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
            camera.getPosition().y -= cameraMoveSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            camera.getRotation().y -= cameraRotateSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            camera.getRotation().y += cameraRotateSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            camera.getRotation().x += cameraRotateSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            camera.getRotation().x -= cameraRotateSpeed * dt;
        }

        float m = 20;
        float vd = (float) (distance * Math.cos(camera.getRotation().x));
        float hd = (float) (distance * Math.sin(camera.getRotation().x));
        float x = (float) (Math.sin(distance * player.getRotation().y));
        float y = (float) (Math.cos(distance * player.getRotation().y));
        distance -= MouseListener.getScrollY() * 2;
        if (MouseListener.mouseButtonDown(0)) {
            camera.getRotation().x -= MouseListener.getDy() * dt;
            //camera.getRotation().y -= MouseListener.getDx() * dt;
        }
        camera.setPosition(x + hd, -y + vd, -distance);
    }

    @Override
    public void cleanUp() {

    }
}
