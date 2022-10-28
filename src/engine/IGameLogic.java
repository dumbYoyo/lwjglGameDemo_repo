package engine;

import engine.input.MouseInput;

public interface IGameLogic { // interfaces ummmmmmm
    void init();
    void input(Window window, MouseInput mouseInput);
    void update(Window window, float dt, MouseInput mouseInput);
    void render(Window window);
    void cleanUp();
}
