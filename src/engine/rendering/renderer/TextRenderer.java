package engine.rendering.renderer;

import engine.rendering.Shader;
import engine.rendering.Texture;
import engine.text.TextData;
import engine.utility.Mathf;
import engine.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class TextRenderer {
    private Shader shader;

    public TextRenderer(Shader shader) {
        this.shader = shader;
    }

    public void render(Map<TextData, List<Text>> texts) {
        for (TextData textData : texts.keySet()) {
            GL20.glActiveTexture(GL13.GL_TEXTURE0);
            GL20.glBindTexture(GL11.GL_TEXTURE_2D, textData.getBitmapTexture().getTextureID());

            GL30.glBindVertexArray(textData.getMesh().getVao());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);

            List<Text> batch = texts.get(textData);
            for (Text text : batch) {
                Matrix4f model = Mathf.getModelMatrix(new Vector3f(text.getPosition(), 0), new Vector3f(0, 0, 0), new Vector3f(text.getScale(), 1));
                shader.loadMatrix4f("model", model);

                GL11.glDrawElements(GL11.GL_TRIANGLES, text.getTextData().getMesh().vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
        }
    }
}
