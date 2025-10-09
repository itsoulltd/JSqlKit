package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.ExpressionProxy;
import com.infoworks.sql.query.models.Expression;
import com.infoworks.sql.query.models.Logic;

import java.util.function.Supplier;

public interface WhereExpressionBuilder extends GroupByBuilder{
	GroupByBuilder where(Logic logic, String... name);
	GroupByBuilder where(Logic logic, ExpressionProxy... comps);
	GroupByBuilder where(Expression expression);
	GroupByBuilder where(Supplier<Expression> supplier);
}