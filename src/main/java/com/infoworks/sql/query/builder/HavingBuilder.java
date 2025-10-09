package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.ExpressionInterpreter;

public interface HavingBuilder extends OrderByBuilder{
	OrderByBuilder having(ExpressionInterpreter expression);
}
