package com.infoworks.entity;

import com.infoworks.entity.Entity;

import java.util.Map;

@FunctionalInterface
public interface ColumnToPropertyMapper {
    Map<String, String> mapColumnsToProperties(Class<? extends Entity> entityType);
}
