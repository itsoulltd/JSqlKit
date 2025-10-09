package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.Expression;
import com.infoworks.orm.Property;

public interface ScalarExpressionBuilder extends WhereExpressionBuilder {
	QueryBuilder where(Property prop, Expression comps);
}