package game;

import engine.*;
import engine.input.MouseListener;
import engine.objects.Camera;
import engine.objects.Entity;
import engine.objects.Light;
import engine.rendering.renderer.MasterRenderer;
import engine.rendering.utility.Texture;
import engine.terrain.Terrain;
import engine.text.Text;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

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

        renderer = new MasterRenderer();

        entity = new Entity(ModelLoader.load("res/models/dragon.obj"));
        entity.setPosition(0, 0, -10);
        entity.setRotation(0, 180, 0);
        entity.setSpecularStrength(128);
        entity.setReflectivity(1);
        entity.getEntityData().setTexture(new Texture("res/textures/ball.png"));

        text = new Text("Hello World", new Vector2f(20, Window.height-690), new Vector2f(0.5f));

        terrain = new Terrain(new Vector3f(0, 0, 0), 100);

        player = new Entity(ModelLoader.load("res/models/Player.obj"));
        player.setRotation(0, 0, 0);
        player.setPosition(10, 0, 0);
        player.getEntityData().setTexture(new Texture("res/textures/Player.png"));
    }

    @Override
    public void update(Window window, float dt) {
        entity.getRotation().y += 3 * dt;

        cameraMovement(dt);

        float playerSpeed = 10;
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            player.getPosition().z -= playerSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            player.getPosition().z += playerSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            player.getPosition().x += playerSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            player.getPosition().x -= playerSpeed * dt;
        }
    }

    @Override
    public void render(Window window) {
        Window.clear();

        renderer.addEntity(entity);
        renderer.addTerrain(terrain);
        renderer.addText(text);
        renderer.addEntity(player);

        renderer.render(light, camera);
    }

    float distance = 50;
    float aap = 0f;
    float s = 4;
    private void cameraMovement(float dt) {
        float m = 20;
        float vd = (float) (distance * Math.sin(Math.toRadians(camera.getPitch())));
        float hd = (float) (distance * Math.cos(Math.toRadians(camera.getPitch())));
        float theta = player.getRotation().y + aap;
        float x = (float) (hd * Math.sin(Math.toRadians(theta)));
        float z = (float) (hd * Math.cos(Math.toRadians(theta)));
        distance -= MouseListener.getScrollY() * 6;
        if (MouseListener.mouseButtonDown(0)) {
            camera.setPitch(camera.getPitch() - (MouseListener.getDy() * s * dt));
        }
        if (MouseListener.mouseButtonDown(1))
            aap += MouseListener.getDx() * s  * dt;
        camera.setYaw(180 - theta);
        camera.setPosition(player.getPosition().x - x, player.getPosition().y + vd, player.getPosition().z - z);

        if (distance <= 5)
            distance = 5;
    }

    @Override
    public void cleanUp() {

    }
}
