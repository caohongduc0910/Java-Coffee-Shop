package CoffeeShop.Util;

import java.util.List;
import java.util.Map;
import java.util.Set;

//Lớp hỗ trợ
public class Common {

    // Kiếm tra obj null hoặc rỗng
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) return true;
        
        if (obj instanceof String) return obj.toString().isEmpty();
        
        if (obj instanceof List) return ((List) obj).isEmpty();
        
        if (obj instanceof Set) return ((Set) obj).isEmpty();

        if (obj instanceof Map) return ((Map) obj).isEmpty();

        return false;
    }

    // Kiểm tra có thể chuyển về số nguyên không
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
