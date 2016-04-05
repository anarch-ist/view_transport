package ru.logist.sbat.testDataGenerator;

import java.util.*;

public class Util {

    private static Random random = new Random();

    public static String getRandomElementFromSet(Set<String> set) {
        int size = set.size();
        int item = random.nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (String obj : set) {
            if (i == item)
                return obj;
            i = i + 1;
        }
        throw new NullPointerException();
    }

    public static Long getRandomElementFromList(List<Long> list) {
        return getRandomElementsFromList(list, 1).get(0);
    }


    public static List<Long> getRandomElementsFromList(List<Long> list, int nSamplesNeeded) {
        List<Long> result = new ArrayList<>();
        int size = list.size();
        Set<Integer> selectedIndexes = new HashSet<>();
        int nSamplesCount = nSamplesNeeded;
        while (nSamplesCount > 0) {
            int randomIndex = random.nextInt(size);
            if (!selectedIndexes.contains(randomIndex)) {
                selectedIndexes.add(randomIndex);
                result.add(list.get(randomIndex));
                nSamplesCount--;
            }
        }
        return result;
    }

}
