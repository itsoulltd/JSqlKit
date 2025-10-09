package com.infoworks.sql.query.models;

public interface Predicate extends Expression {
	public Predicate and(Expression exp);
	public Predicate or(Expression exp);
	public Predicate not();
	public WhereClause and(String key);
	public WhereClause or(String key);
}
