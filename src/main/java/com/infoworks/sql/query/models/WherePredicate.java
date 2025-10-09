package com.infoworks.sql.query.models;

@FunctionalInterface
public interface WherePredicate {
    Predicate apply(Property nextPagingKey);
}
