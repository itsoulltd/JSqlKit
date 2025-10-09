package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.ExpressionProxy;
import com.infoworks.orm.Property;

public interface ScalarExpressionBuilder extends WhereExpressionBuilder {
	QueryBuilder where(Property prop, ExpressionProxy comps);
}