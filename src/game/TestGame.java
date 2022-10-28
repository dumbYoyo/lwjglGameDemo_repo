package game;

import engine.*;
import engine.input.MouseInput;
import engine.objects.Camera;
import engine.objects.GameObject;
import engine.objects.Light;
import engine.renderer.*;
import engine.terrain.Terrain;
import engine.text.Text;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TestGame implements IGameLogic {
    private Renderer renderer;
    private Shader shader;
    private GameObject gameObject;
    private GameObject g;
    private Camera camera;
    private Light light;
    private Texture texture;
    private Terrain terrain;

    private Text text;
    private int score = 0;

    private List<GameObject> gameObjects = new ArrayList<>();

    @Override
    public void init() {
        camera = new Camera();
        renderer = new Renderer(camera);

        shader = new Shader("res/shaders/vertex.vs", "res/shaders/fragment.fs");
        shader.compile();

        texture = new Texture("res/textures/white.png");

        gameObject = new GameObject(ModelLoader.load("res/models/dragon.obj"));
        gameObject.setPosition(0, 0, 0);
        gameObject.setRotation(0, 180, 0);
        gameObject.setSpecularStrength(128);
        gameObject.setReflectivity(1);

        g = new GameObject(new Mesh(new Shape(Shape.SQUARE)));

        light = new Light(new Vector3f(2, 10, -2), new Vector3f(1, 1, 1));

        gameObjects.add(gameObject);
        gameObjects.add(g);

        text = new Text("I am a Genius", new Vector2f(20, Window.height-690), new Vector2f(0.5f));

        terrain = new Terrain();
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
    }

    @Override
    public void update(Window window, float dt, MouseInput mouseInput) {
        gameObject.getRotation().y += 3 * dt;

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
    }

    @Override
    public void render(Window window) {
        renderer.clear();
        //renderer.render(window, gameObject, shader, light, texture);
        //renderer.render(window, g, shader, light, texture);
        renderer.render(text);

        renderer.render(terrain, shader, texture, light);
    }

    @Override
    public void cleanUp() {
        shader.cleanUp();
        renderer.cleanUp();
    }
}
