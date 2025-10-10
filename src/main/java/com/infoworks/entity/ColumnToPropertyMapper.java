package com.infoworks.entity;

import java.util.Map;

@FunctionalInterface
public interface ColumnToPropertyMapper {
    Map<String, String> mapColumnsToProperties(Class<? extends Entity> entityType);
}
