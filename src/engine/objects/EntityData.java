package engine.objects;

import engine.rendering.Texture;

import java.util.ArrayList;
import java.util.List;

public class EntityData {
    private List<Mesh> meshes = new ArrayList<>();
    private Texture texture = null;

    public void cleanUp() {
        if (!meshes.isEmpty() && texture != null) {
            for (Mesh mesh : meshes) {
                mesh.cleanUp();
            }
            texture.cleanUp();
        }
    }

    public Mesh getMesh() {
        return meshes.get(0);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setMesh(Mesh mesh) {
        meshes.clear();
        meshes.add(mesh);
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }
}
