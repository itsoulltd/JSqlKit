package com.infoworks.sql.query.builder;

import com.infoworks.entity.Entity;
import com.infoworks.orm.Property;

public interface ColumnsBuilder extends QueryBuilder{
	TableBuilder columns(String... name);
	InsertBuilder into(String name);
	InsertBuilder into(Class<? extends Entity> cType);
	WhereExpressionBuilder rowsFrom(String name);
	WhereExpressionBuilder rowsFrom(Class<? extends Entity> cType);
	TableBuilder set(Property... properties);
}