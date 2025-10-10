package com.infoworks.entity;

import com.infoworks.sql.executor.QueryExecutor;

import java.sql.SQLException;

public interface SQLEntity {
	String tableName();
    Boolean insert(QueryExecutor exe, String...keys) throws SQLException;
    Boolean update(QueryExecutor exe, String...keys) throws SQLException;
	Boolean delete(QueryExecutor exe) throws SQLException;
}
