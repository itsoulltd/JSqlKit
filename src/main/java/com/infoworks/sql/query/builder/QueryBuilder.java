package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.SQLQuery;

public interface QueryBuilder{
	<T extends SQLQuery> T build();
}