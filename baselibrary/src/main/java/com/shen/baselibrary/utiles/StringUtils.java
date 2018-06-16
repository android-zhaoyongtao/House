package com.shen.baselibrary.utiles;

import java.util.List;

/**
 * @author
 */

public class StringUtils {


    public static int listSize(List<? extends Object> list) {
        if (list != null) {
            return list.size();
        } else {
            return -1;
        }
    }

    public static <T> int arraySize(T[] array) {
        if (array != null) {
            return array.length;
        } else {
            return -1;
        }
    }
}
