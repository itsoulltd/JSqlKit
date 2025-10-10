package com.infoworks.sql.query.models;

import com.infoworks.entity.Entity;

import java.util.Map;

@FunctionalInterface
public interface ColumnToPropertyMapper {
    Map<String, String> mapColumnsToProperties(Class<? extends Entity> entityType);
}
