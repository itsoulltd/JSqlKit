package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.Expression;

public interface HavingBuilder extends OrderByBuilder{
	OrderByBuilder having(Expression expression);
}
