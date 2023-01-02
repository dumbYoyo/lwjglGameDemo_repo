package engine.rendering;

import engine.math.Mathf;
import engine.utility.Shader;
import engine.utility.Texture;
import engine.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.util.List;

public class TextRenderer {
    private Shader shader;
    private Mathf mathf;
    private Texture bitmap;

    public TextRenderer(Shader shader, Texture bitmap) {
        this.shader = shader;
        this.bitmap = bitmap;
        mathf = new Mathf();
    }

    // TODO: make text rendering even more efficient
    public void render(List<Text> texts) {
        for (Text text : texts) {
            GL20.glActiveTexture(GL13.GL_TEXTURE0);
            GL20.glBindTexture(GL11.GL_TEXTURE_2D, bitmap.getTextureID());

            Matrix4f model = mathf.getModelMatrix(new Vector3f(text.getPosition(), 0), new Vector3f(0, 0, 0), new Vector3f(text.getScale(), 1));
            shader.loadMatrix4f("model", model);

            /*
            GL30.glBindVertexArray(text.getMesh().getVao());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);

            GL11.glDrawElements(GL11.GL_TRIANGLES, text.getMesh().vertexCount, GL11.GL_UNSIGNED_INT, 0);

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
             */

            text.getMesh().render();
        }
    }
}
