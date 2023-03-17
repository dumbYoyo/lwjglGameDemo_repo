package engine.rendering.renderer;

import engine.gui.Button;
import engine.rendering.Shader;
import engine.utility.Mathf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.util.List;

public class GuiRenderer {
    private Shader shader;

    public GuiRenderer(Shader shader) {
        this.shader = shader;
    }

    public void render(List<Button> guis, MasterRenderer masterRenderer) {
        for (Button button : guis) {
            if (button.getText() != null) {
                masterRenderer.addText(button.getText());
            }

            if (button.getTexture() != null) {
                GL20.glActiveTexture(GL13.GL_TEXTURE0);
                GL20.glBindTexture(GL11.GL_TEXTURE_2D, button.getTexture().getTextureID());
            }

            Matrix4f model = Mathf.getModelMatrix(new Vector3f(button.getPosition(), 0), new Vector3f(button.getRotation(), 0), new Vector3f(button.getSize(), 1));
            shader.loadMatrix4f("model", model);

            shader.loadVec3f("color", button.getColor());

            if (button.hovering()) {
                button.setColor(button.getHoverColor());
            } else {
                button.setColor(button.getOriginalColor());
            }

            button.getMesh().render();
        }
    }
}
