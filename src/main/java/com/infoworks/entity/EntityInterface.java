package com.infoworks.entity;

import com.infoworks.sql.executor.QueryExecutor;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Map;

public interface EntityInterface {
	String tableName();
    Boolean insert(QueryExecutor exe, String...keys) throws SQLException;
    Boolean update(QueryExecutor exe, String...keys) throws SQLException;
	Boolean delete(QueryExecutor exe) throws SQLException;
	Field getDeclaredField(String fieldName, boolean inherit) throws NoSuchFieldException;
    Field[] getDeclaredFields(boolean inherit);
	Map<String, Object> marshallingToMap(boolean inherit);
    void unmarshallingFromMap(Map<String, Object> data, boolean inherit);
}
