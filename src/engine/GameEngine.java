package engine;

import org.lwjgl.glfw.GLFW;

public class GameEngine implements Runnable {
    private Window window;
    private static Scene scene;
    private final Thread gameLoopThread;
    private boolean vSync;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, Scene scene) {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(width, height, windowTitle);
        GameEngine.scene = scene;
        this.vSync = vSync;
    }

    public static void changeScene(Scene scene) {
        GameEngine.scene.cleanUp();
        GameEngine.scene = scene;
        GameEngine.scene.init();
    }

    public void start() {
        gameLoopThread.start();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    private void gameLoop() {
        double secsPerUpdate = 1.0d / 60.0d;
        double previous = GLFW.glfwGetTime();
        double steps = 0.0d;
        while (!GLFW.glfwWindowShouldClose(window.getWindow())) {
            double now = GLFW.glfwGetTime();
            double elapsed = now - previous;
            previous = now;
            steps += elapsed;

            while (steps >= secsPerUpdate) {
                update((float) elapsed);
                steps -= secsPerUpdate;
            }

            render();

            GLFW.glfwSwapBuffers(window.getWindow());
            GLFW.glfwPollEvents();

            // now deprecated
            /* if (vSync) sync(now); */
        }
    }

    private void sync(double now) {
        float loopSlot = 1f / 60;
        double endTime = now + loopSlot;
        while (GLFW.glfwGetTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void update(float dt) {
        scene.update(window, dt);
    }

    protected void render() {
        scene.render(window);
    }

    protected void cleanUp() {
        scene.cleanUp();
        window.cleanUp();
    }

    protected void init() {
        window.create();
        scene.init();
        if (vSync) {
            window.setSwapInterval(1);
        }
    }
}
