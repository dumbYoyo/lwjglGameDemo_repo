package engine.saver;

import org.joml.Vector3f;

public class SaveData {
    private String name;
    private float[] vertices, texCoords, normals;
    private int[] indices;
    private Vector3f position, rotation, scale, tint, color;
    private float reflectivity;
    private float specularStrength;
    private String texturePath;
    private String collisionMask;

    public SaveData(String name, float[] vertices, float[] texCoords, float[] normals, int[] indices, Vector3f position, Vector3f rotation, Vector3f scale,
                    Vector3f tint, Vector3f color, float reflectivity, float specularStrength, String texturePath, String collisionMask) {
        this.name = name;
        this.vertices = vertices;
        this.texCoords = texCoords;
        this.normals = normals;
        this.indices = indices;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.tint = tint;
        this.color = color;
        this.reflectivity = reflectivity;
        this.specularStrength = specularStrength;
        this.texturePath = texturePath;
        this.collisionMask = collisionMask;
    }

    public String getCollisionMask() {
        return collisionMask;
    }

    public String getName() {
        return name;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public float[] getNormals() {
        return normals;
    }

    public int[] getIndices() {
        return indices;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Vector3f getTint() {
        return tint;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getSpecularStrength() {
        return specularStrength;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
