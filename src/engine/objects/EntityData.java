package engine.objects;

import engine.rendering.utility.Mesh;
import engine.rendering.utility.Texture;

import java.util.ArrayList;
import java.util.List;

public class EntityData {
    private Mesh mesh;
    private List<Mesh> meshes = new ArrayList<>();
    private Texture texture;

    public Mesh getMesh() {
        if (meshes.size() <= 1) {
            return meshes.get(0);
        }
        return null;
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
