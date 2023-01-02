package engine.fbos;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ShadowFbo extends Fbo {
    private int depthTexture;

    public ShadowFbo() {
        super(1024, 1024); // resolution of depth texture

        depthTexture = super.createDepthTexture();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, super.fbo);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
        GL30.glDrawBuffer(GL11.GL_NONE);
        GL30.glReadBuffer(GL11.GL_NONE);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public int getDepthTexture() {
        return depthTexture;
    }
}
