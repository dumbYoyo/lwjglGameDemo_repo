package engine.gui;

import engine.input.MouseListener;
import engine.objects.Mesh;
import engine.rendering.Shape;
import engine.rendering.Texture;
import engine.text.Text;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Button {
    private Texture texture = null;
    private Vector2f position = new Vector2f(0, 0);
    private Vector2f rotation = new Vector2f(0, 0);
    private Vector2f size = new Vector2f(1, 1);
    private Vector3f color = new Vector3f(0, 0, 0);
    private Vector3f originalColor = new Vector3f(0, 0, 0);
    private Vector3f hoverColor = new Vector3f(0.2f, 0.2f, 0.2f);
    private Text text = null;

    private Mesh mesh;

    public Button(Vector3f color) {
        mesh = new Mesh(new Shape(Shape.SQUARE));
        size = new Vector2f(size.x * 100, size.y * 100);
        this.color = color;
        this.originalColor = color;
    }

    public Button(Texture texture) {
        mesh = new Mesh(new Shape(Shape.SQUARE));
        size = new Vector2f(size.x * 100, size.y * 100);
        this.texture = texture;
    }

    public boolean hovering() {
        return (MouseListener.getX() < (position.x + (size.x / 2)) && MouseListener.getX() > (position.x - (size.x / 2))
                && MouseListener.getY() < (position.y + (size.y / 2)) && MouseListener.getY() > (position.y - (size.y / 2)));
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        text.setPosition(new Vector2f(position.x - (text.getWidth() * text.getScale().x) / 2f,
                position.y - (text.getHeight() + text.getScale().y) / 2f));
        this.text = text;
    }

    public void cleanUp() {
        if (text != null) {
            text.cleanUp();
        }
        mesh.cleanUp();
    }

    public Vector3f getOriginalColor() {
        return originalColor;
    }

    public void setOriginalColor(Vector3f originalColor) {
        this.originalColor = originalColor;
    }

    public boolean clicked(int button) {
        return hovering() && MouseListener.mouseButtonClicked(button);
    }

    public boolean clicked() {
        return hovering() && MouseListener.mouseButtonClicked(GLFW.GLFW_MOUSE_BUTTON_1);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vector2f getRotation() {
        return rotation;
    }

    public void setRotation(Vector2f rotation) {
        this.rotation = rotation;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size.mul(100);
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Vector3f hoverColor) {
        this.hoverColor = hoverColor;
    }
}
