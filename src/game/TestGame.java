package game;

import engine.*;
import engine.Window;
import engine.fbos.ShadowFbo;
import engine.gui.Button;
import engine.input.MouseListener;
import engine.objects.Camera;
import engine.objects.Entity;
import engine.objects.Light;
import engine.rendering.*;
import engine.rendering.renderer.MasterRenderer;
import engine.saver.GameSaver;
import engine.saver.SaveData;
import engine.utility.*;
import engine.text.Text;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;

import java.util.Map;

public class TestGame implements Scene {
    private Camera camera;
    private Light light;
    private MasterRenderer renderer;
    private Entity player;
    private EntityManager entityManager;
    private ShadowFbo shadowFbo;
    private Button button;

    private Text text;

    private int score = 0;
    private float playerSpeed = 10;
    private float accl = 0.2f;
    private float fallSpeed = 5;
    private boolean gameStarted = false;

    private Shader shadowShader;

    @Override
    public void init() {
        entityManager = new EntityManager();

        camera = new Camera();
        camera.setPosition(0, -2, -5);

        light = new Light(new Vector3f(-2.0f, 4.0f, -1.0f), new Vector3f(1, 1, 1));

        text = new Text("Change Scene", new Vector2f(0, 0), new Vector2f(0.5f));

        player = new Entity("Entity", new Shape(Shape.CUBE), entityManager);
        player.setPosition(2, 1, 0);
        player.setColor(new Vector3f(1, 0, 0));

        renderer = new MasterRenderer();

        button = new Button(new Vector3f(54 / 255f, 54 / 255f, 54 / 255f));
        button.setHoverColor(new Vector3f(80 / 255f, 80 / 255f, 80 / 255f));
        button.setPosition(new Vector2f(200, 100));
        button.setSize(new Vector2f(3, 1));
        button.setText(text);

        Map<String, SaveData> save = GameSaver.loadSaveFrom("res/saves/save5.txt");
        for (String name : save.keySet()) {
            SaveData data = save.get(name);
            new Entity(name, entityManager, data.getVertices(), data.getTexCoords(), data.getNormals(), data.getIndices(),
                    data.getPosition(), data.getRotation(), data.getScale(), data.getTint(), data.getColor(), data.getReflectivity(), data.getSpecularStrength(),
                    new Texture(data.getTexturePath()), data.getCollisionMask());
        }

        shadowFbo = new ShadowFbo();
        shadowShader = new Shader("res/shaders/shadow/vertex.vs", "res/shaders/shadow/fragment.fs");
        shadowShader.compile();
    }

    @Override
    public void update(Window window, float dt) {
        if (button.clicked()) {
            GameEngine.changeScene(new MainMenu());
        }

        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS && !gameStarted) gameStarted = true;
        //if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) player.getPosition().x -= playerSpeed * dt;
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) player.getPosition().z += playerSpeed * dt;
        //if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) player.getPosition().x += playerSpeed * dt;
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) player.getPosition().z -= playerSpeed * dt;
        if (Math.abs(player.getPosition().y) > 10) restart();
        if (gameStarted) player.getPosition().x -= playerSpeed * dt;

        if (Math.abs(player.getPosition().z) - 0.1f > 3) {
            fallSpeed += accl;
            player.getPosition().y -= fallSpeed * dt;
        }

        for (String name : entityManager.getEntities_name().keySet()) {
            Entity obstacle = entityManager.getEntity(name);
            if (obstacle.getCollisionMask().equals("Obstacle")) {
                if (Entity.checkCollision(player, obstacle)) {
                    restart();
                }
            }
        }

        camera.setPosition(6.92f + player.getPosition().x, 5f, 0.18f);
        camera.setPitch(30.64f);
        camera.setYaw(271.22f);
    }

    @Override
    public void render(Window window) {
        // shadow phase
        shadowFbo.bindFbo();
        window.clear();
        GL11.glEnable(GL11.GL_FRONT);
        renderer.setEntityShader(shadowShader);
        renderScene();
        shadowFbo.unbindBondFbo();

        // game render phase
        GL11.glEnable(GL11.GL_BACK);
        window.clear();
        renderer.setEntityShader(renderer.getOriginalEntityShader());
        renderer.getEntityShader().loadInteger("shadow_sampler", 1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, shadowFbo.getDepthTexture());
        renderScene();

        renderer.addGui(button);
        renderer.addText(text);
        renderer.renderGuis();
    }

    private void renderScene() {
        //renderer.addText(text);

        for (String name : entityManager.getEntities_name().keySet()) {
            Entity e = entityManager.getEntity(name);
            renderer.addEntity(e);
        }

        Vector3f dir = new Vector3f(-2.0f, 4.0f, -1.0f);
        renderer.getEntityShader().loadVec3f("direction", dir);
        renderer.getTerrainShader().loadVec3f("direction", dir);

        renderer.renderEntities(light, camera, player);
    }

    private void restart() {
        fallSpeed = 5;
        player.setPosition(2, 1, 0);
        gameStarted = false;
    }

    @Override
    public void cleanUp() {
        for (String name : entityManager.getEntities_name().keySet()) {
            entityManager.getEntity(name).cleanUp();
        }
        button.cleanUp();
        shadowFbo.cleanUp();
        shadowShader.cleanUp();
        renderer.cleanUp();
    }
}
