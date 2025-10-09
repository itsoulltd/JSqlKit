package com.infoworks.sql.query.builder;

import com.infoworks.entity.Entity;
import com.infoworks.sql.query.models.JoinExpression;

public interface JoinOnBuilder extends WhereExpressionBuilder{
	JoinBuilder on(JoinExpression expression);
	JoinBuilder on(String leftColumn, String rightColumn);
	JoinOnBuilder rejoin(String table);
	JoinOnBuilder rejoin(Class<? extends Entity> cType);
}
