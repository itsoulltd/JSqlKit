package com.infoworks.sql.query.pagination;

import com.infoworks.objects.Message;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PagingQuery extends Message implements Pagination<PagingQuery> {

    private Integer page = 0;
    private Integer size = 0;
    private List<SortDescriptor> descriptors = new ArrayList<>();

    public PagingQuery() {}

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public List<SortDescriptor> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<SortDescriptor> descriptors) {
        if (descriptors == null || descriptors.isEmpty()) return;
        this.descriptors.addAll(descriptors);
    }

    @Override
    public boolean containValidStuff(String value) {
        //TODO: CHECK all sort-descriptor keys for malicious stuff:
        return true;
    }

    @Override
    public PagingQuery next() {
        PagingQuery query = jumpTo(++page);
        return query;
    }

    @Override
    public PagingQuery jumpTo(Integer page) {
        if (page < 0) return null;
        PagingQuery query = null;
        try {
            query = cloneMe(getClass());
            query.setPage(page);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    public PagingQuery previous() {
        if (page == 0) return null;
        PagingQuery query = jumpTo(--page);
        return query;
    }

    protected <T extends PagingQuery> T cloneMe(Class<T> type)
            throws IllegalAccessException, InstantiationException {
        T myClone = null;
        try {
            myClone = type.getDeclaredConstructor().newInstance();
            myClone.unmarshalling(this.marshalling(true), true);
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new InstantiationException(e.getMessage());
        }
        return myClone;
    }

}
