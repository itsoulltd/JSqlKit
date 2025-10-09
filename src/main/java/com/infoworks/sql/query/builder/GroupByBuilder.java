package com.infoworks.sql.query.builder;

public interface GroupByBuilder extends OrderByBuilder{
	HavingBuilder groupBy(String...columns);
}
