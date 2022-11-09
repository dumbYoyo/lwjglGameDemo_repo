package engine;

import engine.math.Utils;
import engine.rendering.utility.Mesh;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {
    public static List<Mesh> load(String modelPath) {
        AIScene aiScene = Assimp.aiImportFile(modelPath, Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate);
        if (aiScene == null) {
            System.out.println("Error loading model at: " + modelPath);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        List<Mesh> meshes = new ArrayList<>(numMeshes);
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(aiMesh);
            meshes.add(mesh);
        }

        return meshes;
    }

    /*
    public static Mesh[] load(String modelPath) {
        AIScene aiScene = Assimp.aiImportFile(modelPath, Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate);
        if (aiScene == null) {
            System.out.println("Error loading model at: " + modelPath);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes(); // PointerBuffer is just like a pointer in C++/C
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(aiMesh);
            meshes[i] = mesh;
        }

        return meshes;
    }
     */

    private static Mesh processMesh(AIMesh aiMesh) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoordinates = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        processVertices(aiMesh, vertices);
        processTextureCoordinates(aiMesh, textureCoordinates);
        processNormals(aiMesh, normals);
        processIndices(aiMesh, indices);

        return new Mesh(Utils.floatListToArray(vertices), Utils.floatListToArray(textureCoordinates), Utils.floatListToArray(normals), Utils.integerListToArray(indices));
    }

    private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }

    private static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        while (aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }

    private static void processTextureCoordinates(AIMesh aiMesh, List<Float> textureCoordinates) {
        AIVector3D.Buffer aiTextureCoordinates = aiMesh.mTextureCoords(0);

        for (int i = 0; i < aiTextureCoordinates.limit(); i++) {
            AIVector3D aiTextureCoordinate = aiTextureCoordinates.get(i);

            textureCoordinates.add(aiTextureCoordinate.x());
            textureCoordinates.add(aiTextureCoordinate.y());
        }
    }

    private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        AIFace.Buffer aiIndices = aiMesh.mFaces();

        for (int i = 0; i < aiIndices.limit(); i++) {
            AIFace aiIndex = aiIndices.get(i);

            for (int j = 0; j < aiIndex.mNumIndices(); j++) {
                indices.add(aiIndex.mIndices().get(j));
            }
        }
    }
}
