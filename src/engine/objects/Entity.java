package engine.objects;

import engine.utility.EntityManager;
import engine.utility.Mesh;
import engine.utility.Shape;
import engine.utility.Texture;
import org.joml.Vector3f;
import java.util.List;

public class Entity {
    private EntityData entityData = new EntityData();
    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Vector3f scale = new Vector3f(1, 1, 1);
    private Vector3f tint = new Vector3f(1, 1, 1);
    private Vector3f color = new Vector3f(0, 0, 0);
    private float specularStrength = 1, reflectivity = 0;
    private String collisionMask = "null";

    private String name;
    private static int nameCounter;
    private int id;
    private static int IDCounter = 0;

    public Entity(String name, Shape shape, EntityManager manager) {
        if (!manager.getEntities_name().containsKey(name)) {
            this.name = name;
        } else {
            this.name = name + nameCounter++;
        }
        entityData.setMesh(new Mesh(shape.getVertices(), shape.getTextureCoordinates(), shape.getNormals(), shape.getIndices()));

        id = IDCounter++;
        manager.addEntity(this);
    }

    public Entity(String name, EntityManager manager, float[] vertices, float[] texCoords, float[] normals, int[] indices, Vector3f position, Vector3f rotation,
                  Vector3f scale, Vector3f tint, Vector3f color, float reflectivity, float specularStrength, Texture texture, String collisionMask) {
        if (!manager.getEntities_name().containsKey(name)) {
            this.name = name;
        } else {
            this.name = name + nameCounter++;
        }
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.tint = tint;
        this.color = color;
        this.reflectivity = reflectivity;
        this.specularStrength = specularStrength;
        this.collisionMask = collisionMask;
        entityData.setMesh(new Mesh(vertices, texCoords, normals, indices));
        entityData.setTexture(texture);

        id = IDCounter++;
        manager.addEntity(this);
    }

    public Entity(String name, Mesh mesh, EntityManager manager) {
        if (!manager.getEntities_name().containsKey(name)) {
            this.name = name;
        } else {
            this.name = name + nameCounter++;
        }
        entityData.setMesh(mesh);

        id = IDCounter++;
        manager.addEntity(this);
    }

    public Entity(String name, List<Mesh> meshes, EntityManager manager) {
        if (!manager.getEntities_name().containsKey(name)) {
            this.name = name;
        } else {
            this.name = name + nameCounter++;
        }
        entityData.setMeshes(meshes);

        id = IDCounter++;
        manager.addEntity(this);
    }

    public static boolean checkCollision(Entity a, Entity b) {
        //check the X axis
        if(Math.abs(a.getPosition().x - b.getPosition().x) < a.getScale().x/2f + b.getScale().x/2f) {
            //check the Y axis
            if(Math.abs(a.getPosition().y - b.getPosition().y) < a.getScale().y/2f + b.getScale().y/2f) {
                //check the Z axis
                if (Math.abs(a.getPosition().z - b.getPosition().z) < a.getScale().z/2f + b.getScale().z/2f) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getCollisionMask() {
        return collisionMask;
    }

    public void setCollisionMask(String collisionMask) {
        this.collisionMask = collisionMask;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = new Vector3f(scale);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Vector3f getTint() {
        return tint;
    }

    public void setTint(Vector3f tint) {
        this.tint = tint;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    public float getSpecularStrength() {
        return specularStrength;
    }

    public void setSpecularStrength(float specularStrength) {
        this.specularStrength = specularStrength;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
