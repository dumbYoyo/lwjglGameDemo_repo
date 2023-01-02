package engine.utility;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private int vao, vbo, tbo, nbo, ibo;
    public int vertexCount;
    private float[] vertices, texCoords, normals;
    private int[] indices;

    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> vaos = new ArrayList<>();

    public Mesh(float[] vertices, float[] textureCoordinates, float[] normals, int[] indices) {
        this.vertices = vertices;
        this.texCoords = textureCoordinates;
        this.normals = normals;
        this.indices = indices;
        this.vertexCount = indices.length;
        FloatBuffer verticesBuffer = null;
        FloatBuffer textureCoordinatesBuffer = null;
        FloatBuffer normalsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vao = GL30.glGenVertexArrays();
            vaos.add(vao);
            GL30.glBindVertexArray(vao);

            vbo = GL15.glGenBuffers();
            vbos.add(vbo);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
            GL20.glEnableVertexAttribArray(0);

            tbo = GL15.glGenBuffers();
            vbos.add(tbo);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tbo);
            textureCoordinatesBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
            textureCoordinatesBuffer.put(textureCoordinates).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureCoordinatesBuffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
            GL20.glEnableVertexAttribArray(1);

            nbo = GL15.glGenBuffers();
            vbos.add(nbo);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, nbo);
            normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            normalsBuffer.put(normals).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
            GL20.glEnableVertexAttribArray(2);

            ibo = GL15.glGenBuffers();
            vbos.add(ibo);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        } finally {
            MemoryUtil.memFree(verticesBuffer);
            MemoryUtil.memFree(textureCoordinatesBuffer);
            MemoryUtil.memFree(normalsBuffer);
            MemoryUtil.memFree(indicesBuffer);
        }
    }

    // this is what i was scared of
    public void render() {
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public int getVertexCount() {
        return vertexCount;
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

    public void cleanUp() {
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
    }

    public int getVao() {
        return vao;
    }
}
