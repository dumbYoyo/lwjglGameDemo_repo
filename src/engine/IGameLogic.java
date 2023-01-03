package engine;

public interface IGameLogic { // interfaces ummmmmmm
    void init();
    void update(Window window, float dt);
    void render(Window window);
    void cleanUp();
}
