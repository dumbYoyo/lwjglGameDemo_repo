package engine.math;

import java.util.List;

public class Utils {
    public static float[] floatListToArray(List<Float> list) {
        int listCapacity = list.size();
        float[] array = new float[listCapacity];
        for (int i = 0; i < listCapacity; i++) {
            array[i] = list.get(i);
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
