package com.infoworks.sql.query.builder;

import com.infoworks.entity.Entity;
import com.infoworks.sql.query.models.Join;

public interface JoinOnBuilder extends WhereExpressionBuilder{
	JoinBuilder on(Join expression);
	JoinBuilder on(String leftColumn, String rightColumn);
	JoinOnBuilder rejoin(String table);
	JoinOnBuilder rejoin(Class<? extends Entity> cType);
}
