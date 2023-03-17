package engine.text;

import engine.rendering.Texture;
import engine.utility.Utils;
import engine.objects.Mesh;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Text {
    private static final int SPACE_ASCII = 32;
    private static final float Z_POS = 0f;
    private static final float SPACE_GAP = 10;
    private static final int VERTICES_PER_QUAD = 4;

    private Map<Integer, Character> charInfo = new HashMap<>();
    private List<Float> vertices = new ArrayList<>();
    private List<Float> texCoords = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private Vector2f position;
    private Vector2f scale;
    private float sX = 0, sY = 0;
    private TextData textData;
    private float textHeight, textWidth;

    public Text(String text, Vector2f position, Vector2f scale) {
        this.scale = scale;
        this.position = position;
        Texture fontTexture = new Texture("res/text/verdana.png", GL11.GL_NEAREST);
        fontTexture.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL15.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL15.GL_CLAMP_TO_EDGE);
        fontTexture.unbind();
        textData = new TextData("res/text/verdana.fnt", fontTexture);
        new TextReader(textData.getBitmapFontPath(), charInfo);
        generateMeshData(text);
    }

    public void setText(String text) {
        textData.getMesh().cleanUp();
        generateMeshData(text);
    }

    private void generateMeshData(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.US_ASCII);

        List<Float> textHeights = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            Character character = charInfo.get((int) bytes[i]);
            //sY = position.y - (character.getHeight() / 2f);

            // top left vertex
            float x1 = character.getPosition().x;
            float y1 = character.getPosition().y + character.getHeight();
            vertices.add(sX + character.getOffset().x);
            vertices.add(sY + character.getHeight() + character.getOffset().y);
            vertices.add(Z_POS);
            float xt1 = (x1) * (1 / 512f);
            float yt1 = (y1) * (1 / 512f);
            indices.add(i*VERTICES_PER_QUAD);

            // bottom left vertex
            float x2 = character.getPosition().x;
            float y2 = character.getPosition().y;
            vertices.add(sX + character.getOffset().x);
            vertices.add(sY + character.getOffset().y);
            vertices.add(Z_POS);
            float xt2 = (x2) * (1 / 512f);
            float yt2 = (y2) * (1 / 512f);
            indices.add(i*VERTICES_PER_QUAD+1);

            // bottom right vertex
            float x3 = character.getPosition().x + character.getWidth();
            float y3 = character.getPosition().y;
            vertices.add(sX + character.getWidth() + character.getOffset().x);
            vertices.add(sY + character.getOffset().y);
            vertices.add(Z_POS);
            float xt3 = (x3) * (1 / 512f);
            float yt3 = (y3) * (1 / 512f);
            indices.add(i*VERTICES_PER_QUAD+2);

            // top right vertex
            float x4 = character.getPosition().x + character.getWidth();
            float y4 = character.getPosition().y + character.getHeight();
            vertices.add(sX + character.getWidth() + character.getOffset().x);
            vertices.add(sY + character.getHeight() + character.getOffset().y);
            vertices.add(Z_POS);
            float xt4 = (x4) * (1 / 512f);
            float yt4 = (y4) * (1 / 512f);
            indices.add(i*VERTICES_PER_QUAD+3);

            indices.add(i*4);
            indices.add(i*VERTICES_PER_QUAD+2);

            // they are not in order, cuz, origin in opengl's texture coordinate system is in bottom left, but origin in bitmaps' texture
            // coordinate system is in top left, hence we need to move texture coordinates' order here are there
            texCoords.add(xt1); // x
            texCoords.add(yt1); // y

            texCoords.add(xt2); // x
            texCoords.add(yt2); // y

            texCoords.add(xt3); // x
            texCoords.add(yt3); // y

            texCoords.add(xt4); // x
            texCoords.add(yt4); // y

            sX += character.getxAdvance();
            textWidth += character.getWidth() + character.getOffset().x;
            textHeights.add(character.getHeight());
        }
        textHeight = Utils.getAverageOfFloatList(textHeights);

        float[] normals = new float[0];
        textData.setMesh(new Mesh(Utils.floatListToArray(vertices), Utils.floatListToArray(texCoords), normals, Utils.integerListToArray(this.indices)));
    }

    public void cleanUp() {
        textData.cleanUp();
    }

    public float getWidth() {
        return textWidth;
    }

    public float getHeight() {
        return textHeight;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public TextData getTextData() {
        return textData;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector2f getPosition() {
        return position;
    }
}
