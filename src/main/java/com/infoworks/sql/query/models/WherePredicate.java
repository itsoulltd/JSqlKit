package com.infoworks.sql.query.models;

import com.infoworks.orm.Property;

@FunctionalInterface
public interface WherePredicate {
    Predicate apply(Property nextPagingKey);
}
