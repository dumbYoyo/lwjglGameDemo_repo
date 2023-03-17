package engine.fbos;

import engine.Window;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

public abstract class Fbo {
    private int width, height;
    protected int fbo;

    public Fbo(int width, int height) {
        this.width = width;
        this.height = height;

        fbo = createFrameBuffer();
    }

    // we're returning int bcz we're returning entityID, which is int, from the fragment shader. Hence, we return an int
    public int readPixel(int x, int y) {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo);
        GL30.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);

        float[] pixels = new float[3];
        GL30.glReadPixels(x, y, 1, 1, GL11.GL_RGB, GL11.GL_FLOAT, pixels);

        return ((int) pixels[0]) - 1;
    }

    public void cleanUp() {
        GL30.glDeleteFramebuffers(fbo);
    }

    public void bindFbo() {
        // make sure no texture is bond, bcz if any texture is bond, stuff gets f*d up man
        GL20.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo);
        GL11.glViewport(0, 0, width, height);
    }

    public void unbindBondFbo() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Window.width, Window.height);
    }

    protected int createFrameBuffer() {
        int fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        return fbo;
    }

    protected int createTexture() {
        int texture = GL20.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB32F, width, height, 0, GL11.GL_RGB, GL11.GL_FLOAT, 0);
        GL40.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
        return texture;
    }

    protected int createDepthTexture() {
        int texture = GL20.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL20.GL_DEPTH_COMPONENT, width, height, 0, GL20.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, 0);
        GL40.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, texture, 0);
        return texture;
    }

    protected int createDepthBuffer() {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }
}
