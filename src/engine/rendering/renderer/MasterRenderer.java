package engine.rendering.renderer;

import engine.Window;
import engine.gui.Button;
import engine.rendering.Shader;
import engine.rendering.Texture;
import engine.text.TextData;
import engine.utility.Mathf;
import engine.objects.Camera;
import engine.objects.Entity;
import engine.objects.EntityData;
import engine.objects.Light;
import engine.terrain.Terrain;
import engine.terrain.TerrainGenerator;
import engine.text.Text;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {
    private EntityRenderer entityRenderer;
    private Shader entityShader, originalEntityShader;
    private TerrainRenderer terrainRenderer;
    private Shader terrainShader;
    private TextRenderer textRenderer;
    private Shader textShader;
    private GuiRenderer guiRenderer;
    private Shader guiShader;

    private Map<EntityData, List<Entity>> entities = new HashMap<>();
    private Map<TerrainGenerator, List<Terrain>> terrains = new HashMap<>();
    private Map<TextData, List<Text>> texts = new HashMap<>();
    private List<Button> guis = new ArrayList<>();

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
        textRenderer = new TextRenderer(textShader);

        guiShader = new Shader("res/shaders/gui/vertex.vs", "res/shaders/gui/fragment.fs");
        guiShader.compile();
        guiRenderer = new GuiRenderer(guiShader);

        setupProjections();
    }

    public void renderEntities(Light light, Camera camera, Entity player) {
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

        entities.clear();
        terrains.clear();
    }

    public void renderGuis() {
        textShader.bind();
        textRenderer.render(texts);
        textShader.unbind();

        guiShader.bind();
        guiRenderer.render(guis, this);
        guiShader.unbind();

        if (texts.size() > 1)
            texts.clear();
        guis.clear();
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

        guiShader.bind();
        guiShader.loadMatrix4f("proj", ortho);
        guiShader.unbind();
    }

    public Shader getOriginalEntityShader() {
        return originalEntityShader;
    }

    public void setEntityShader(Shader shader) {
        this.entityShader = shader;
        entityRenderer.setShader(shader);
    }

    public void cleanUp() {
        entityShader.cleanUp();
        textShader.cleanUp();
        terrainShader.cleanUp();
        guiShader.cleanUp();
    }

    public Shader getEntityShader() {
        return entityShader;
    }

    public Shader getTerrainShader() {
        return terrainShader;
    }

    public void addGui(Button button) {
        guis.add(button);
    }

    public void addText(Text text) {
        TextData textData = text.getTextData();
        List<Text> batch = texts.get(textData);
        if (batch != null) {
            batch.add(text);
        } else {
            List<Text> newBatch = new ArrayList<>();
            newBatch.add(text);
            texts.put(textData, newBatch);
        }
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
