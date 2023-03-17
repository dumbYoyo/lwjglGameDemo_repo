package engine.fbos;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class ShadowFbo extends Fbo {
    private int depthTexture;

    public ShadowFbo() {
        super(3840, 3840); // resolution of depth texture

        depthTexture = super.createDepthTexture();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL15.GL_CLAMP_TO_BORDER);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL15.GL_CLAMP_TO_BORDER);
        float[] borderColor = {
                1.0f, 1.0f, 1.0f, 1.0f
        };
        GL11.glTexParameterfv(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, borderColor);

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, super.fbo);
        GL30.glDrawBuffer(GL11.GL_NONE);
        GL30.glReadBuffer(GL11.GL_NONE);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        GL11.glDeleteTextures(depthTexture);
    }

    public int getDepthTexture() {
        return depthTexture;
    }
}
