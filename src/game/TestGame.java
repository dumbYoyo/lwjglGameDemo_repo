package game;

import engine.*;
import engine.Window;
import engine.archive.Renderer;
import engine.fbos.ShadowFbo;
import engine.input.MouseListener;
import engine.math.Mathf;
import engine.objects.Camera;
import engine.objects.Entity;
import engine.objects.Light;
import engine.rendering.MasterRenderer;
import engine.saver.GameSaver;
import engine.saver.SaveData;
import engine.utility.*;
import engine.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;

import java.util.Map;

public class TestGame implements IGameLogic {
    private Camera camera;
    private Light light;
    private MasterRenderer renderer;
    private Entity player, dragon;
    private EntityManager entityManager;
    private Map<String, SaveData> save;
    private ShadowFbo shadowFbo;

    private Text text;

    private int score = 0;
    private float playerSpeed = 10;
    private float accl = 0.2f;
    private float fallSpeed = 5;
    private boolean gameStarted = false;

    Shader shadowShader;

    Shader shader;
    Mesh mesh;
    Shape shape = new Shape(Shape.SQUARE);
    Texture texture;

    @Override
    public void init() {
        entityManager = new EntityManager();

        camera = new Camera();
        camera.setPosition(0, -2, -5);

        light = new Light(new Vector3f(-2.0f, 4.0f, -1.0f), new Vector3f(1, 1, 1));

        renderer = new MasterRenderer();

        text = new Text("Hit SPACE to start", new Vector2f(20, Window.height-690), new Vector2f(0.5f));

        player = new Entity("Entity", new Shape(Shape.CUBE), entityManager);
        player.setPosition(2, 1, 0);
        player.setColor(new Vector3f(1, 0, 0));

        save = GameSaver.loadSaveFrom("res/saves/save5.txt");
        for (String name : save.keySet()) {
            SaveData data = save.get(name);
            new Entity(name, entityManager, data.getVertices(), data.getTexCoords(), data.getNormals(), data.getIndices(),
                    data.getPosition(), data.getRotation(), data.getScale(), data.getTint(), data.getColor(), data.getReflectivity(), data.getSpecularStrength(),
                    new Texture(data.getTexturePath()), data.getCollisionMask());
        }

        shadowFbo = new ShadowFbo();
        shadowShader = new Shader("res/shaders/shadow/vertex.vs", "res/shaders/shadow/fragment.fs");
        shadowShader.compile();

        shader = new Shader("res/shaders/text/vertex.vs", "res/shaders/text/fragment.fs");
        shader.compile();

        mesh = new Mesh(shape.getVertices(), shape.getTextureCoordinates(), shape.getNormals(), shape.getIndices());
        texture = new Texture("res/textures/ball.png");
    }

    @Override
    public void update(Window window, float dt) {
        cameraMovement(dt);
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            player.getPosition().x += playerSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            player.getPosition().x -= playerSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS && !gameStarted) {
            gameStarted = true;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            player.getPosition().z += playerSpeed * dt;
        }
        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            player.getPosition().z -= playerSpeed * dt;
        }

        if (Math.abs(player.getPosition().z) - 0.1f > 3) {
            fallSpeed += accl;
            player.getPosition().y -= fallSpeed * dt;
        }

        if (Math.abs(player.getPosition().y) > 10) {
            restart();
        }

        if (gameStarted) {
            player.getPosition().x -= playerSpeed * dt;
        }

        for (String name : entityManager.getEntities_name().keySet()) {
            Entity obstacle = entityManager.getEntity(name);
            if (obstacle.getCollisionMask().equals("Obstacle")) {
                if (Entity.checkCollision(player, obstacle)) {
                    restart();
                }
            }
        }

        //camera.setPosition(6.92f + player.getPosition().x, 5f, 0.18f);
        //camera.setPitch(30.64f);
        //camera.setYaw(271.22f);
    }

    Matrix4f lightProj = new Matrix4f();
    Matrix4f lightView = new Matrix4f();
    Matrix4f lightSpace = new Matrix4f();
    Mathf mathf = new Mathf();
    @Override
    public void render(Window window) {


        shadowFbo.bindFbo();
        Window.clearDepth();
        renderer.setEntityShader(shadowShader);

        renderScene();
        shadowFbo.unbindBondFbo();

        Window.clear();
        renderer.setEntityShader(renderer.getOriginalEntityShader());
        renderer.getEntityShader().loadInteger("shadow_sampler", 1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, shadowFbo.getDepthTexture());
        renderScene();






        texture.setTextureID(shadowFbo.getDepthTexture());
        shader.bind();
        shader.loadInteger("texture_sampler", 0);
        Matrix4f model = mathf.getModelMatrix(new Vector3f(0), new Vector3f(0, 0, 0), new Vector3f(1));
        shader.loadMatrix4f("model", model);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        //mesh.render();
        shader.unbind();
    }

    private void renderScene() {
        renderer.addText(text);

        for (String name : entityManager.getEntities_name().keySet()) {
            Entity e = entityManager.getEntity(name);
            renderer.addEntity(e);
        }

        Vector3f dir = new Vector3f(-2.0f, -4.0f, -1.0f);
        renderer.getEntityShader().loadVec3f("direction", dir);
        renderer.getTerrainShader().loadVec3f("direction", dir);

        renderer.render(light, camera);
    }

    private void restart() {
        fallSpeed = 5;
        player.setPosition(2, 1, 0);
        gameStarted = false;
    }

    float distance = 50;
    float angleAroundPlayer = 0f;
    float sensitivity = 8;
    private void cameraMovement(float dt) {
        float m = 20;
        float vd = (float) (distance * Math.sin(Math.toRadians(camera.getPitch())));
        float hd = (float) (distance * Math.cos(Math.toRadians(camera.getPitch())));
        float theta = player.getRotation().y + angleAroundPlayer;
        float x = (float) (hd * Math.sin(Math.toRadians(theta)));
        float z = (float) (hd * Math.cos(Math.toRadians(theta)));
        distance -= MouseListener.getScrollY() * 6;
        if (MouseListener.mouseButtonDown(0)) {
            camera.setPitch(camera.getPitch() - (MouseListener.getDy() * sensitivity * dt));
        }
        if (MouseListener.mouseButtonDown(0))
            angleAroundPlayer += MouseListener.getDx() * sensitivity * dt;
        camera.setYaw(180 - theta);
        camera.setPosition(player.getPosition().x - x, player.getPosition().y + vd, player.getPosition().z - z);

        if (distance <= 5)
            distance = 5;
    }

    @Override
    public void cleanUp() {

    }
}
