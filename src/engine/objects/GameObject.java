package engine.objects;

import engine.renderer.Mesh;
import engine.renderer.Shape;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GameObject {
    private Mesh mesh;
    private Mesh[] meshes;
    private Vector3f position;
    private Vector3f rotation;
    private Vector4f color;
    private float scale;
    private float specularStrength, reflectivity;

    public GameObject() {
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        color = new Vector4f(1, 1, 1, 1);
        specularStrength = 128;
        reflectivity = 1;
    }

    public GameObject(Shape shape) {
        this.mesh = new Mesh(shape);
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        color = new Vector4f(1, 1, 1, 1);
        specularStrength = 128;
        reflectivity = 1;
    }

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        color = new Vector4f(1, 1, 1, 1);
        specularStrength = 128;
        reflectivity = 1;
    }

    public GameObject(Mesh[] meshes) {
        this.meshes = meshes;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        color = new Vector4f(1, 1, 1, 1);
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
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

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Mesh[] getMeshes() {
        return meshes;
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

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }
}
