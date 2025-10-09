package com.infoworks.sql.query.builder;

import com.infoworks.orm.Property;

public interface InsertBuilder extends QueryBuilder{
	QueryBuilder values(Property...properties);
}