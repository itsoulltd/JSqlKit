package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.Operator;

public interface OrderByBuilder extends LimitBuilder {
	LimitBuilder orderBy(String...columns);
	LimitBuilder orderBy(Operator order, String...columns);
}
