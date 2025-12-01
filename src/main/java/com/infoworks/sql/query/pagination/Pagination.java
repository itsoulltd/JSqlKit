package com.infoworks.sql.query.pagination;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public interface Pagination<P extends Pagination> {

    P next();
    P jumpTo(Integer page);
    P previous();

    Integer getPage();
    void setPage(Integer page);
    Integer getSize();
    void setSize(Integer size);
    List<SortDescriptor> getDescriptors();
    void setDescriptors(List<SortDescriptor> descriptors);
    boolean containValidStuff(String value);

    static <T extends Pagination> T of(Class<T> type, int page, int size, SortOrder order, String...keys) {
        Pagination query = null;
        try {
            query = type.getDeclaredConstructor().newInstance();
            query.setPage(page);
            query.setSize(size);
            SortDescriptor des = new SortDescriptor(order);
            des.setKeys(Arrays.asList(keys));
            query.setDescriptors(Arrays.asList(des));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T) query;
    }

    default boolean validate(Object value){
        return value != null && containValidStuff(value.toString());
    }
}
