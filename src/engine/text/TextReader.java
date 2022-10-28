package engine.text;

import org.joml.Vector2f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextReader {
    public TextReader(String path, Map<Integer, Character> charInfo) {
        List<String> lines = new ArrayList<>();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;
        int curntLineNr = 0;

        try {
            if (bufferedReader != null) {
                while ((line = bufferedReader.readLine()) != null) {
                    if (curntLineNr++ < 4) {
                        continue;
                    }
                    lines.add(line);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> contents = new ArrayList<>();
        for (String crrLine : lines) {
            String[] currentLine = crrLine.split(" ");
            for (String crtString : currentLine) {
                if (!crtString.trim().isEmpty()) {
                    String[] splited = crtString.split("=");
                    for (String s : splited) {
                        contents.add(s);
                    }
                }
            }
        }

        for (int i = 0; i < contents.size() - 1; i = i + 20) {
            int charID = Integer.parseInt(contents.get(i+1));
            int posX = Integer.parseInt(contents.get(i + 3));
            int posY = Integer.parseInt(contents.get(i + 5));
            int width = Integer.parseInt(contents.get(i + 7));
            int height = Integer.parseInt(contents.get(i + 9));
            int xOffset = Integer.parseInt(contents.get(i + 11));
            int yOffset = Integer.parseInt(contents.get(i + 13));
            int xAdvance = Integer.parseInt(contents.get(i + 15));

            charInfo.put(charID, new Character(new Vector2f(posX, posY), new Vector2f(xOffset, yOffset), xAdvance, width, height, charID));
        }
    }
}
