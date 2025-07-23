package finpik.util.db;

public class QuerydslUtil {
    public static boolean hasNext(Integer contentSize, Integer pageSize) {
        return contentSize > pageSize;
    }

    public static long getLimit(int pageSize) {
        return (long) pageSize + 1;
    }
}
