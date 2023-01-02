package engine.saver;

import engine.math.Utils;
import engine.objects.Entity;
import engine.utility.EntityManager;
import engine.utility.Mesh;
import org.joml.Vector3f;

import java.io.*;
import java.util.*;

public class GameSaver {
    private GameSaver() {
    }

    public static void saveGameTo(String path, EntityManager entityManager) {
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                System.out.println("Saved successfully at: " + path);
            }

            FileWriter writer = new FileWriter(file);

            for (String name : entityManager.getEntities_name().keySet()) {
                Entity entity = entityManager.getEntity(name);
                Vector3f position = entity.getPosition();
                Vector3f rotation = entity.getRotation();
                Vector3f scale = entity.getScale();
                Vector3f tint = entity.getTint();
                Vector3f color = entity.getColor();

                writer.write(name + "\n");
                writer.write("{\n");

                for (Mesh mesh : entity.getEntityData().getMeshes()) {
                    writer.write("Vertices: ");
                    for (float vertex : mesh.getVertices()) {
                        writer.write(vertex + " ");
                    }
                    writer.write("\n");

                    writer.write("TexCoords: ");
                    for (float texCoord : mesh.getTexCoords()) {
                        writer.write(texCoord + " ");
                    }
                    writer.write("\n");

                    writer.write("Normals: ");
                    for (float normal : mesh.getNormals()) {
                        writer.write(normal + " ");
                    }
                    writer.write("\n");

                    writer.write("Indices: ");
                    for (int index : mesh.getIndices()) {
                        writer.write(index + " ");
                    }
                    writer.write("\n");
                }
                writer.write("Position: " + position.x + " " + position.y + " " + position.z + "\n");
                writer.write("Rotation: " + rotation.x + " " + rotation.y + " " + rotation.z + "\n");
                writer.write("Scale: " + scale.x + " " + scale.y + " " + scale.z + "\n");
                writer.write("Tint: " + tint.x + " " + tint.y + " " + tint.z + "\n");
                writer.write("Color: " + color.x + " " + color.y + " " + color.z + "\n");
                writer.write("Reflectivity: " + entity.getReflectivity() + "\n");
                writer.write("SpecularStrength: " + entity.getSpecularStrength() + "\n");
                writer.write("TexturePath: " + entity.getEntityData().getTexture().getPath() + "\n");
                writer.write("CollisionMask: " + entity.getCollisionMask() + "\n");

                writer.write("}\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Couldn't save file at: " + path);
            e.printStackTrace();
        }
    }

    // Note - there must be no spaces in texturePath.
    public static Map<String, SaveData> loadSaveFrom(String path) {
        Map<String, SaveData> map = new HashMap<>();

        List<String> lines = new ArrayList<>();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;
        try {
            if (bufferedReader != null) {
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> content = new ArrayList<>();
        for (String l : lines) {
            String[] currLine = l.split(" ");
            content.addAll(Arrays.asList(currLine));
        }

        // 259, 261
        for (int i = 0; i < content.size(); i += 263) {
            map.put(content.get(i), new SaveData(content.get(i), Utils.stringListToFloatArray(content.subList(i + 3, i + 75)),
                    Utils.stringListToFloatArray(content.subList(i + 76, i + 124)), Utils.stringListToFloatArray(content.subList(i + 125, i + 197)),
                    Utils.stringListToIntegerArray(content.subList(i + 198, i + 234)), new Vector3f(Float.parseFloat(content.get(i + 235)), Float.parseFloat(content.get(i + 236)), Float.parseFloat(content.get(i + 237))),
                    new Vector3f(Float.parseFloat(content.get(i + 239)), Float.parseFloat(content.get(i + 240)), Float.parseFloat(content.get(i + 241))),
                    new Vector3f(Float.parseFloat(content.get(i + 243)), Float.parseFloat(content.get(i + 244)), Float.parseFloat(content.get(i + 245))),
                    new Vector3f(Float.parseFloat(content.get(i + 247)), Float.parseFloat(content.get(i + 248)), Float.parseFloat(content.get(i + 249))),
                    new Vector3f(Float.parseFloat(content.get(i + 251)), Float.parseFloat(content.get(i + 252)), Float.parseFloat(content.get(i + 253))),
                    Float.parseFloat(content.get(i + 255)), Float.parseFloat(content.get(i + 257)), content.get(i + 259), content.get(i + 261)));
        }

        return map;
    }
}
