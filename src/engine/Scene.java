package engine;

public interface Scene { // interfaces ummmmmmm
    void init();
    void update(Window window, float dt);
    void render(Window window);
    void cleanUp();
}
