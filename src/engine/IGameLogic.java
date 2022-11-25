package engine;

import engine.archive.MouseInput;

public interface IGameLogic { // interfaces ummmmmmm
    void init();
    void update(Window window, float dt);
    void render(Window window);
    void cleanUp();
}
