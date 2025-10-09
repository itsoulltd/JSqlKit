package com.infoworks.sql.query.models;

public interface Expression extends ExpressionProxyResolver {
	String interpret();
}
