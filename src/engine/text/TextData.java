package engine.text;

import engine.objects.Mesh;
import engine.rendering.Texture;

public class TextData {
    private String bitmapFontPath;
    private Texture bitmapTexture;
    private Mesh mesh;

    public TextData(String bitmapPath, Texture bitmapTexture) {
        this.bitmapFontPath = bitmapPath;
        this.bitmapTexture = bitmapTexture;
    }

    public void cleanUp() {
        bitmapTexture.cleanUp();
        mesh.cleanUp();
    }

    public Texture getBitmapTexture() {
        return bitmapTexture;
    }

    public void setBitmapTexture(Texture bitmapTexture) {
        this.bitmapTexture = bitmapTexture;
    }

    public String getBitmapFontPath() {
        return bitmapFontPath;
    }

    public void setBitmapFontPath(String bitmapFontPath) {
        this.bitmapFontPath = bitmapFontPath;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
