package com.infoworks.sql.query.builder;

import com.infoworks.sql.query.models.Property;

public interface InsertBuilder extends QueryBuilder{
	QueryBuilder values(Property...properties);
}