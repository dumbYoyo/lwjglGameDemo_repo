import engine.GameEngine;
import engine.IGameLogic;
import engine.text.TextReader;
import game.TestGame;
import org.lwjgl.stb.STBEasyFont;
import org.lwjgl.stb.STBTruetype;

public class Main {
    public static void main(String[] args) {
        try {
            IGameLogic gameLogic = new TestGame();
            GameEngine gameEngine = new GameEngine("voxel engine", 1280, 720, true, gameLogic);
            gameEngine.start();
            //new TextReader("res/text/verdana.fnt");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something bad happened.");
            System.exit(-1);
        }

    }
}
