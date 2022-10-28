package engine.text;

import org.joml.Vector2f;

public class Character {
    private Vector2f position;
    private Vector2f offset;
    private float xAdvance;
    private float width, height;
    private int ascii;

    public Character(Vector2f position, Vector2f offset, float xAdvance, float width, float height, int ascii) {
        this.position = position;
        this.offset = offset;
        this.xAdvance = xAdvance;
        this.width = width;
        this.height = height;
        this.ascii = ascii;
    }

    public int getAscii() {
        return ascii;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public float getxAdvance() {
        return xAdvance;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
