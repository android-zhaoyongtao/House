package house.shen.com.house.utiles;

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

}
