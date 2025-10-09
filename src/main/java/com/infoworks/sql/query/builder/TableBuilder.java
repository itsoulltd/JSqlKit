package com.infoworks.sql.query.builder;

import com.infoworks.entity.Entity;

public interface TableBuilder extends QueryBuilder{
	WhereExpressionBuilder from(String name);
	WhereExpressionBuilder from(Class<? extends Entity> cType);
	ScalarExpressionBuilder on(String name);
	ScalarExpressionBuilder on(Class<? extends Entity> cType);
}