package com.infoworks.sql.query.builder;

public interface LimitBuilder extends QueryBuilder {
	QueryBuilder addLimit(Integer limit, Integer offset);
}
