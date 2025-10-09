package com.infoworks.cql.entity;

import com.infoworks.sql.query.models.DataType;
import com.infoworks.sql.query.models.Operator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ClusteringKey {
    String name();
    DataType type() default DataType.STRING;
    Operator order() default Operator.ASC;
}
