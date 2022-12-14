package engine.rendering;

import engine.Window;
import engine.utility.Mathf;
import engine.objects.Camera;
import engine.objects.Entity;
import engine.objects.EntityData;
import engine.objects.Light;
import engine.terrain.Terrain;
import engine.terrain.TerrainGenerator;
import engine.text.Text;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {
    private Shader entityShader, originalEntityShader;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private Shader terrainShader;
    private TextRenderer textRenderer;
    private Shader textShader;

    private Map<EntityData, List<Entity>> entities = new HashMap<>();
    private Map<TerrainGenerator, List<Terrain>> terrains = new HashMap<>();
    private List<Text> texts = new ArrayList<>();

    public MasterRenderer() {
        entityShader = new Shader("res/shaders/entity/vertex.vs", "res/shaders/entity/fragment.fs");
        entityShader.compile();
        originalEntityShader = entityShader;
        originalEntityShader.compile();
        entityRenderer = new EntityRenderer(entityShader);

        terrainShader = new Shader("res/shaders/terrain/vertex.vs", "res/shaders/terrain/fragment.fs");
        terrainShader.compile();
        terrainRenderer = new TerrainRenderer(terrainShader);

        textShader = new Shader("res/shaders/text/vertex.vs", "res/shaders/text/fragment.fs");
        textShader.compile();
        textRenderer = new TextRenderer(textShader, new Texture("res/text/verdana.png"));

        setupProjections();
    }

    public void render(Light light, Camera camera, Entity player) {
        Matrix4f view = Mathf.getViewMatrix(camera);

        entityShader.bind();
        entityShader.loadMatrix4f("view", view);
        entityShader.loadVec3f("lightPos", light.getPosition());
        entityShader.loadVec3f("lightColor", light.getColor());
        entityRenderer.render(entities, player);
        entityShader.unbind();

        terrainShader.bind();
        terrainShader.loadMatrix4f("view", view);
        terrainShader.loadVec3f("lightPos", light.getPosition());
        terrainShader.loadVec3f("lightColor", light.getColor());
        terrainRenderer.render(terrains);
        terrainShader.unbind();

        textShader.bind();
        textRenderer.render(texts);
        textShader.unbind();

        entities.clear();
        terrains.clear();
        texts.clear();
    }

    public void setupProjections() {
        Matrix4f proj = Mathf.getProjMatrix(45, Window.width / (float) Window.height, 0.1f, 1000f);
        Matrix4f ortho = Mathf.getOrthoMatrix(0, Window.width, Window.height, 0);

        entityShader.bind();
        entityShader.loadMatrix4f("proj", proj);
        entityShader.unbind();

        terrainShader.bind();
        terrainShader.loadMatrix4f("proj", proj);
        terrainShader.unbind();

        textShader.bind();
        textShader.loadMatrix4f("proj", ortho);
        textShader.unbind();
    }

    public Shader getOriginalEntityShader() {
        return originalEntityShader;
    }

    public void setEntityShader(Shader shader) {
        this.entityShader = shader;
        entityRenderer.setShader(shader);
    }

    public void addText(Text text) {
        texts.add(text);
    }

    public Shader getEntityShader() {
        return entityShader;
    }

    public Shader getTerrainShader() {
        return terrainShader;
    }

    public void addTerrain(Terrain terrain) {
        TerrainGenerator terrainData = terrain.getTerrainGenerator();
        List<Terrain> batch = terrains.get(terrainData);
        if (batch != null) {
            batch.add(terrain);
        } else {
            List<Terrain> newBatch = new ArrayList<>();
            newBatch.add(terrain);
            terrains.put(terrainData, newBatch);
        }
    }

    public void addEntity(Entity entity) {
        EntityData entityData = entity.getEntityData();
        List<Entity> batch = entities.get(entityData);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityData, newBatch);
        }
    }
}
