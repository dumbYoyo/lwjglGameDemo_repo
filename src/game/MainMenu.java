package game;

import engine.GameEngine;
import engine.Scene;
import engine.Window;
import engine.gui.Button;
import engine.rendering.renderer.MasterRenderer;
import engine.text.Text;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MainMenu implements Scene {
    private Button button;
    private Text text;
    private MasterRenderer renderer;

    @Override
    public void init() {
        text = new Text("Change", new Vector2f(0, 0), new Vector2f(0.5f));
        button = new Button(new Vector3f(54 / 255f, 54 / 255f, 54 / 255f));
        button.setHoverColor(new Vector3f(80 / 255f, 80 / 255f, 80 / 255f));
        button.setPosition(new Vector2f(200, 400));
        button.setText(text);
        button.setSize(new Vector2f(2, 1));

        renderer = new MasterRenderer();
    }

    @Override
    public void update(Window window, float dt) {
        if (button.clicked()) {
            GameEngine.changeScene(new TestGame());
        }
    }

    @Override
    public void render(Window window) {
        window.clear();
        renderer.addGui(button);
        renderer.renderGuis();
    }

    @Override
    public void cleanUp() {

    }
}
