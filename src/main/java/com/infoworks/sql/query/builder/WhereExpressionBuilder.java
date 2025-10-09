package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.Expression;
import com.infoworks.sql.query.models.ExpressionInterpreter;
import com.infoworks.sql.query.models.Logic;

import java.util.function.Supplier;

public interface WhereExpressionBuilder extends GroupByBuilder{
	GroupByBuilder where(Logic logic, String... name);
	GroupByBuilder where(Logic logic, Expression... comps);
	GroupByBuilder where(ExpressionInterpreter expression);
	GroupByBuilder where(Supplier<ExpressionInterpreter> supplier);
}