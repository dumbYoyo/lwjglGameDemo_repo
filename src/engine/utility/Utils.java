package engine.utility;

import java.util.List;

public class Utils {
    private Utils() {
    }

    public static float[] floatListToArray(List<Float> list) {
        int listCapacity = list.size();
        float[] array = new float[listCapacity];
        for (int i = 0; i < listCapacity; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static float[] stringListToFloatArray(List<String> list) {
        int listCapacity = list.size();
        float[] array = new float[listCapacity];

        for (int i = 0; i < listCapacity; i++) {
            array[i] = Float.parseFloat(list.get(i));
        }

        return array;
    }

    public static int[] stringListToIntegerArray(List<String> list) {
        int listCapacity = list.size();
        int[] array = new int[listCapacity];

        for (int i = 0; i < listCapacity; i++) {
            array[i] = Integer.parseInt(list.get(i));
        }

        return array;
    }

    public static int[] integerListToArray(List<Integer> list) {
        int listCapacity = list.size();
        int[] array = new int[listCapacity];
        for (int i = 0; i < listCapacity; i++) {
            array[i] = list.get(i);
        }

        return array;
    }
}
