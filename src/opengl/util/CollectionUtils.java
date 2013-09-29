package opengl.util;

import java.util.Collection;
import java.util.Random;

public class CollectionUtils {

    public static <E> E randomElement(Collection<E> collection) {
        int item = new Random().nextInt(collection.size());

        int i = 0;
        for (E element : collection) {
            if (i == item) {
                return element;
            }

            i++;
        }

        return null;
    }

}
